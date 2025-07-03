package com.walking.tbooking.service;

import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.repository.TicketRepository;

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

    public List<FullTicketDto> getActualByUser(long user_id){
        return repository.readActualByUser(user_id);
    }

    public List<FullTicketDto> getAllByUser(long user_id){
        return repository.readAllByUser(user_id);
    }

    public List<FullTicketDto> getAll(){
        return repository.readAll();
    }
}
