package com.walking.tbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walking.tbooking.db.TransactionTemplate;
import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.mapper.TicketJsonMapper;
import com.walking.tbooking.repository.FlightRepository;
import com.walking.tbooking.repository.TicketRepository;

import javax.sql.DataSource;
import java.sql.SQLException;

public class BookingService {
    private static BookingService instance;
    private BookingService(DataSource dataSource,
                           TicketRepository ticketRepository,
                           FlightRepository flightRepository,
                           TicketJsonMapper mapper){
        this.ticketRepository=ticketRepository;
        this.flightRepository=flightRepository;
        this.mapper=mapper;
        this.transactionTemplate=new TransactionTemplate(dataSource);
    }
    public static BookingService getInstance( DataSource dataSource,
                                              TicketRepository ticketRepository,
                                              FlightRepository flightRepository,
                                              TicketJsonMapper mapper){
        if(instance==null)
            instance=new BookingService(dataSource, ticketRepository, flightRepository, mapper);
        return instance;
    }

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final TicketJsonMapper mapper;
    private final TransactionTemplate transactionTemplate;

    public String book(String ticket) throws JsonProcessingException, RuntimeException {
        TicketDto ticketDto= mapper.getTicketDto(ticket);
        FullTicketDto result=transactionTemplate.runTransactional(connection -> {
            ticketRepository.book(ticketDto, connection);
            int availableSeats= flightRepository.readAvailableSeats(ticketDto.getFlight_id(), connection);
            flightRepository.updateAvailableSeats(ticketDto.getFlight_id(),availableSeats-1,connection);
            return ticketRepository.read(ticketDto.getFlight_id(), ticketDto.getSeat(), connection);
        });
        return mapper.getString(result);
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
