package com.walking.tbooking.mapper;

import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.exception.MapperException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TicketMapper {
    public FullTicketDto map(ResultSet rs) throws MapperException, SQLException{
        if (!rs.next())
            throw new MapperException("ResultSet have not value");
        FullTicketDto result=new FullTicketDto();
        result.setFlight_id(rs.getLong("flight_id"));
        result.setDeparture_airport(rs.getString("departure_airport"));
        result.setDeparture_time(rs.getTimestamp("departure_time").toLocalDateTime());
        result.setArrival_airport(rs.getString("arrival_airport"));
        result.setArrival_time(rs.getTimestamp("arrival_time").toLocalDateTime());
        result.setSeat(rs.getInt("seat"));
        result.setService_class(rs.getString("service_class"));
        result.setBaggage_allowance(rs.getString("baggage_allowance"));
        result.setSurname(rs.getString("surname"));
        result.setName(rs.getString("name"));
        result.setPatronymic(rs.getString("patronymic"));
        return result;
    }

    public List<FullTicketDto> mapMany(ResultSet rs) throws MapperException, SQLException{
        FullTicketDto ticket=new FullTicketDto();
        LinkedList<FullTicketDto> result=new LinkedList<>();
        while(rs.next()){
            ticket.setFlight_id(rs.getLong("flight_id"));
            ticket.setDeparture_airport(rs.getString("departure_airport"));
            ticket.setDeparture_time(rs.getTimestamp("departure_time").toLocalDateTime());
            ticket.setArrival_airport(rs.getString("arrival_airport"));
            ticket.setArrival_time(rs.getTimestamp("arrival_time").toLocalDateTime());
            ticket.setSeat(rs.getInt("seat"));
            ticket.setService_class(rs.getString("service_class"));
            ticket.setBaggage_allowance(rs.getString("baggage_allowance"));
            ticket.setSurname(rs.getString("surname"));
            ticket.setName(rs.getString("name"));
            ticket.setPatronymic(rs.getString("patronymic"));
            result.add(ticket);
        }
        if(result.isEmpty())
            throw new MapperException("ResultSet have not value");
        return result;
    }
}
