package com.walking.tbooking.service;

import com.walking.tbooking.dto.ticket.FullTicketDto;

import java.util.List;

public class TicketService {
    private static TicketService instance;
    private TicketService(){

    }
    public static TicketService getInstance(){
        if(instance==null){
            instance=new TicketService();
        }
        return instance;
    }

    public List<FullTicketDto> getActualByUser(long user_id){
        throw new RuntimeException("Not implemented");
    }

    public List<FullTicketDto> getAllByUser(long user_id){
        throw new RuntimeException("Not implemented");
    }

    public List<FullTicketDto> getAll(){
        throw new RuntimeException("Not implemented");
    }
}
