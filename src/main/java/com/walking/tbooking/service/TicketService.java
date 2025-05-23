package com.walking.tbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.TicketJsonMapper;
import com.walking.tbooking.repository.TicketRepository;

import java.sql.SQLException;
import java.util.List;

public class TicketService {
    private static TicketService instance;
    private TicketService(TicketRepository repository, TicketJsonMapper mapper){
        this.repository=repository;
        this.mapper=mapper;
    }
    public static TicketService getInstance(TicketRepository repository, TicketJsonMapper mapper){
        if(instance==null){
            instance=new TicketService(repository, mapper);
        }
        return instance;
    }

    private final TicketRepository repository;
    private final TicketJsonMapper mapper;

    public String getActualByUser(long user_id) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.readActualByUser(user_id));
    }

    public String getAllByUser(long user_id) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.readAllByUser(user_id));
    }

    public String getAll() throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.readAll());
    }
}
