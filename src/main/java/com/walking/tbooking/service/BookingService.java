package com.walking.tbooking.service;

import com.walking.tbooking.db.TransactionTemplate;
import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.repository.FavoriteAirportsRepository;
import com.walking.tbooking.repository.FlightRepository;
import com.walking.tbooking.repository.PassengerRepository;
import com.walking.tbooking.repository.TicketRepository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookingService {
    private static BookingService instance;
    private BookingService(DataSource dataSource,
                           TicketRepository ticketRepository,
                           FlightRepository flightRepository,
                           PassengerRepository passengerRepository,
                           FavoriteAirportsRepository favoriteAirportsRepository){
        this.ticketRepository=ticketRepository;
        this.flightRepository=flightRepository;
        this.passengerRepository=passengerRepository;
        this.favoriteAirportsRepository=favoriteAirportsRepository;
        this.transactionTemplate=new TransactionTemplate(dataSource);
    }
    public static BookingService getInstance( DataSource dataSource,
                                              TicketRepository ticketRepository,
                                              FlightRepository flightRepository,
                                              PassengerRepository passengerRepository,
                                              FavoriteAirportsRepository favoriteAirportsRepository){
        if(instance==null)
            instance=new BookingService(dataSource, ticketRepository,
                    flightRepository, passengerRepository, favoriteAirportsRepository);
        return instance;
    }

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final FavoriteAirportsRepository favoriteAirportsRepository;
    private final TransactionTemplate transactionTemplate;

    public FullTicketDto book(long user_id, TicketDto ticket){
        List<ReadPassengerDto> passengerMatchList=passengerRepository.readByUserId(user_id)
                .stream().filter(passenger->passenger.getId()==ticket.getPassenger_id()).toList();
        if (passengerMatchList.isEmpty())
            throw new RuntimeException("Unauthorized");
        FullTicketDto result=transactionTemplate.runTransactional(connection -> {
            ticketRepository.book(ticket, connection);
            int availableSeats= flightRepository.readAvailableSeats(ticket.getFlight_id(), connection);
            flightRepository.updateAvailableSeats(ticket.getFlight_id(),availableSeats-1,connection);
            return ticketRepository.read(ticket.getFlight_id(), ticket.getSeat(), connection);
        });
        favoriteAirportsRecalculation(ticket.getPassenger_id());
        return result;
    }

    public void unbook(long userId, int roleId, long flight_id, int seat) {
        var ownerId= ticketRepository.readOwnerByTicket(flight_id, seat);
        if (ownerId.get("user_id")!=userId&roleId!=1)
            throw new RuntimeException("Unauthorized");
        transactionTemplate.runTransactional(connection -> {
            ticketRepository.delete(flight_id, seat, connection);
            int availableSeats= flightRepository.readAvailableSeats(flight_id, connection);
            flightRepository.updateAvailableSeats(flight_id,availableSeats+1,connection);
            return null;
        });
        favoriteAirportsRecalculation(ownerId.get("passenger_id"));
    }

    private void favoriteAirportsRecalculation(long passengerId){
        var airportsByPassenger=ticketRepository.airportsByPassenger(passengerId);
        if (airportsByPassenger.isEmpty()){
            favoriteAirportsRepository.delete(passengerId);
            return;
        }
        ArrayList<String> favoriteAirports=(ArrayList<String>)airportsByPassenger.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (favoriteAirports.size()<3)
            favoriteAirports.add(favoriteAirports.get(1));
        favoriteAirportsRepository.updateFavoriteAirports(passengerId, favoriteAirports);
    }
}
