package com.walking.tbooking.mapper;

import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.exception.MapperException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PassengerMapper {
    public ReadPassengerDto map(ResultSet rs) throws MapperException, SQLException{
        if (!rs.next())
            throw new MapperException("ResultSet have not value");
        ReadPassengerDto result=new ReadPassengerDto();
        result.setId(rs.getLong("id"));
        result.setUser_id(rs.getLong("user_id"));
        result.setSurname(rs.getString("surname"));
        result.setName(rs.getString("name"));
        result.setPatronymic(rs.getString("patronymic"));
        result.setMale(rs.getBoolean("male"));
        result.setBirth_date(rs.getDate("birth_date").toLocalDate());
        result.setPassport_series(rs.getInt("passport_series"));
        result.setPassport_number(rs.getInt("passport_number"));
        result.setPassport_source(rs.getString("passport_source"));
        result.setPassport_issue_date(rs.getDate("passport_issue_date").toLocalDate());
        return result;
    }

    public List<ReadPassengerDto> mapMany(ResultSet rs) throws MapperException, SQLException{
        LinkedList<ReadPassengerDto> result=new LinkedList<>();
        while (!rs.next()){
            ReadPassengerDto passenger=new ReadPassengerDto();
            passenger.setId(rs.getLong("id"));
            passenger.setUser_id(rs.getLong("user_id"));
            passenger.setSurname(rs.getString("surname"));
            passenger.setName(rs.getString("name"));
            passenger.setPatronymic(rs.getString("patronymic"));
            passenger.setMale(rs.getBoolean("male"));
            passenger.setBirth_date(rs.getDate("birth_date").toLocalDate());
            passenger.setPassport_series(rs.getInt("passport_series"));
            passenger.setPassport_number(rs.getInt("passport_number"));
            passenger.setPassport_source(rs.getString("passport_source"));
            passenger.setPassport_issue_date(rs.getDate("passport_issue_date").toLocalDate());
            result.add(passenger);
        }
        if(result.isEmpty())
            throw new MapperException("ResultSet have not value");
        return result;
    }
}
