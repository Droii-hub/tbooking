package com.walking.tbooking.service;

import com.walking.tbooking.db.TransactionTemplate;
import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.repository.FlightRepository;
import com.walking.tbooking.repository.TicketRepository;

import javax.sql.DataSource;

public class BookingService {
    private static BookingService instance;
    private BookingService(DataSource dataSource,
                           TicketRepository ticketRepository,
                           FlightRepository flightRepository){
        this.ticketRepository=ticketRepository;
        this.flightRepository=flightRepository;
        this.transactionTemplate=new TransactionTemplate(dataSource);
    }
    public static BookingService getInstance( DataSource dataSource,
                                              TicketRepository ticketRepository,
                                              FlightRepository flightRepository){
        if(instance==null)
            instance=new BookingService(dataSource, ticketRepository, flightRepository);
        return instance;
    }

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final TransactionTemplate transactionTemplate;

    public FullTicketDto book(TicketDto ticket) throws RuntimeException {
        return transactionTemplate.runTransactional(connection -> {
            ticketRepository.book(ticket, connection);
            int availableSeats= flightRepository.readAvailableSeats(ticket.getFlight_id(), connection);
            flightRepository.updateAvailableSeats(ticket.getFlight_id(),availableSeats-1,connection);
            return ticketRepository.read(ticket.getFlight_id(), ticket.getSeat(), connection);
        });
    }

    public void unbook(long flight_id, int seat) {
        transactionTemplate.runTransactional(connection -> {
            ticketRepository.delete(flight_id, seat, connection);
            int availableSeats= flightRepository.readAvailableSeats(flight_id, connection);
            flightRepository.updateAvailableSeats(flight_id,availableSeats+1,connection);
            return null;
        });
    }
}
