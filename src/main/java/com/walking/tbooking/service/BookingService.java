package com.walking.tbooking.service;

import com.walking.tbooking.db.TransactionTemplate;
import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.repository.FlightRepository;
import com.walking.tbooking.repository.PassengerRepository;
import com.walking.tbooking.repository.TicketRepository;

import javax.sql.DataSource;
import java.util.List;

public class BookingService {
    private static BookingService instance;
    private BookingService(DataSource dataSource,
                           TicketRepository ticketRepository,
                           FlightRepository flightRepository,
                           PassengerRepository passengerRepository){
        this.ticketRepository=ticketRepository;
        this.flightRepository=flightRepository;
        this.passengerRepository=passengerRepository;
        this.transactionTemplate=new TransactionTemplate(dataSource);
    }
    public static BookingService getInstance( DataSource dataSource,
                                              TicketRepository ticketRepository,
                                              FlightRepository flightRepository,
                                              PassengerRepository passengerRepository){
        if(instance==null)
            instance=new BookingService(dataSource, ticketRepository, flightRepository, passengerRepository);
        return instance;
    }

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final TransactionTemplate transactionTemplate;

    public FullTicketDto book(long user_id, TicketDto ticket){
        List<ReadPassengerDto> passengerMatchList=passengerRepository.readByUserId(user_id)
                .stream().filter(passenger->passenger.getId()==ticket.getPassenger_id()).toList();
        if (passengerMatchList.isEmpty())
            throw new RuntimeException("Unauthorized");
        return transactionTemplate.runTransactional(connection -> {
            ticketRepository.book(ticket, connection);
            int availableSeats= flightRepository.readAvailableSeats(ticket.getFlight_id(), connection);
            flightRepository.updateAvailableSeats(ticket.getFlight_id(),availableSeats-1,connection);
            return ticketRepository.read(ticket.getFlight_id(), ticket.getSeat(), connection);
        });
    }

    public void unbook(long userId, int roleId, long flight_id, int seat) {
        long ownerId= ticketRepository.readUserIdByTicket(flight_id, seat);
        if (ownerId!=userId&roleId!=1)
            throw new RuntimeException("Unauthorized");
        transactionTemplate.runTransactional(connection -> {
            ticketRepository.delete(flight_id, seat, connection);
            int availableSeats= flightRepository.readAvailableSeats(flight_id, connection);
            flightRepository.updateAvailableSeats(flight_id,availableSeats+1,connection);
            return null;
        });
    }
}
