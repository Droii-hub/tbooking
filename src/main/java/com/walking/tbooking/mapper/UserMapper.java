package com.walking.tbooking.mapper;

import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.exception.MapperException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserMapper {
    public ReadUserDto map(ResultSet rs) throws MapperException, SQLException {
        if (!rs.next())
            throw new MapperException("ResultSet have not value");
        ReadUserDto result=new ReadUserDto();
        result.setId(rs.getLong("id"));
        result.setEmail(rs.getString("email"));
        result.setSurname(rs.getString("surname"));
        result.setName(rs.getString("name"));
        result.setPatronymic(rs.getString("patronymic"));
        var lastEnter=rs.getTimestamp("last_enter");
        result.setLastEnter(lastEnter==null ? null : lastEnter.toLocalDateTime());
        result.setBlocked(rs.getBoolean("blocked"));
        return result;
    }

    public List<ReadUserDto> mapMany(ResultSet rs) throws MapperException, SQLException {
        LinkedList<ReadUserDto> result=new LinkedList<>();
        while (rs.next()){
            ReadUserDto user=new ReadUserDto();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setSurname(rs.getString("surname"));
            user.setName(rs.getString("name"));
            user.setPatronymic(rs.getString("patronymic"));
            var lastEnter=rs.getTimestamp("last_enter");
            user.setLastEnter(lastEnter==null ? null : lastEnter.toLocalDateTime());
            user.setBlocked(rs.getBoolean("blocked"));
            result.add(user);
        }
        if(result.isEmpty())
            throw new MapperException("ResultSet have not value");
        return result;
    }
}
