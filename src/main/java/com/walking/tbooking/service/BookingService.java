package com.walking.tbooking.service;

import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.dto.ticket.FullTicketDto;

public class BookingService {
    private static BookingService instance;
    private BookingService(){

    }
    public static BookingService getInstance(){
        if(instance==null)
            instance=new BookingService();
        return instance;
    }

    public FullTicketDto book(TicketDto ticket){
        throw new RuntimeException("Not implemented");
    }

    public void unbook(long flight_id, int seat, long user_id){
        throw new RuntimeException("Not implemented");
    }
}
