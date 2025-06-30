package com.walking.tbooking.mapper;

import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.exception.MapperException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AirportMapper {
    public AirportDto map(ResultSet rs)throws SQLException{
        if (!rs.next())
            throw new SQLException("ResultSet have not value");
        AirportDto airport=new AirportDto();
        airport.setIata(rs.getString("iata"));
        airport.setName(rs.getString("name"));
        airport.setLocation(rs.getString("location"));
        return airport;
    }

    public List<AirportDto> mapMany(ResultSet rs) throws SQLException{
        LinkedList<AirportDto> result=new LinkedList<>();
        while(rs.next()){
            AirportDto airport=new AirportDto();
            airport.setIata(rs.getString("iata"));
            airport.setName(rs.getString("name"));
            airport.setLocation(rs.getString("location"));
            result.add(airport);
        }
        if(result.isEmpty())
            throw new SQLException("ResultSet have not value");
        return result;
    }
}
