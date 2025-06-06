package com.walking.tbooking.service;

import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.repository.TicketRepository;

import java.sql.SQLException;
import java.util.List;

public class TicketService {
    private static TicketService instance;
    private TicketService(TicketRepository repository){
        this.repository=repository;
    }
    public static TicketService getInstance(TicketRepository repository){
        if(instance==null){
            instance=new TicketService(repository);
        }
        return instance;
    }

    private final TicketRepository repository;

    public List<FullTicketDto> getActualByUser(long user_id) throws SQLException, MapperException {
        return repository.readActualByUser(user_id);
    }

    public List<FullTicketDto> getAllByUser(long user_id) throws SQLException, MapperException {
        return repository.readAllByUser(user_id);
    }

    public List<FullTicketDto> getAll() throws SQLException, MapperException {
        return repository.readAll();
    }
}
