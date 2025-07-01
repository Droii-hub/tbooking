package com.walking.tbooking.mapper;

import com.walking.tbooking.dto.flight.ReadFlightDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FlightMapper {
    public ReadFlightDto map(ResultSet rs) throws SQLException{
        if(!rs.next())
            throw new SQLException("ResultSet have not value");
        ReadFlightDto result=new ReadFlightDto();
        result.setId(rs.getLong("id"));
        result.setDeparture_airport(rs.getString("departure_airport"));
        result.setDeparture_time(rs.getTimestamp("departure_time").toLocalDateTime());
        result.setArrival_airport(rs.getString("arrival_airport"));
        result.setArrival_time(rs.getTimestamp("arrival_time").toLocalDateTime());
        result.setAvailable_seats(rs.getInt("available_seats"));
        result.setTotal_seats(rs.getInt("total_seats"));
        return result;
    }

    public List<ReadFlightDto> mapMany(ResultSet rs) throws SQLException{
        LinkedList<ReadFlightDto> result=new LinkedList<>();
        while(rs.next()){
            ReadFlightDto flight=new ReadFlightDto();
            flight.setId(rs.getLong("id"));
            flight.setDeparture_airport(rs.getString("departure_airport"));
            flight.setDeparture_time(rs.getTimestamp("departure_time").toLocalDateTime());
            flight.setArrival_airport(rs.getString("arrival_airport"));
            flight.setArrival_time(rs.getTimestamp("arrival_time").toLocalDateTime());
            flight.setAvailable_seats(rs.getInt("available_seats"));
            flight.setTotal_seats(rs.getInt("total_seats"));
            result.add(flight);
        }
        if(result.isEmpty())
            throw new SQLException("ResultSet have not value");
        return result;
    }
}
