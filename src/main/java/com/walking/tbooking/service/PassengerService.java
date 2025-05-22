package com.walking.tbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.dto.passenger.SearchPassengerDto;
import com.walking.tbooking.dto.passenger.UpdatePassengerDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.PassengerJsonMapper;
import com.walking.tbooking.mapper.PassengerMapper;
import com.walking.tbooking.repository.PassengerRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class PassengerService {
    private static PassengerService instance;

    private PassengerService(PassengerRepository repository, PassengerJsonMapper mapper){
        this.repository=repository;
        this.mapper=mapper;
    }

    public static PassengerService getInstance(PassengerRepository repository, PassengerJsonMapper mapper){
        if(instance==null){
            instance=new PassengerService(repository, mapper);
        }
        return instance;
    }

    private final PassengerRepository repository;
    private final PassengerJsonMapper mapper;

    public String create(String passenger, long user_id) throws JsonProcessingException, SQLException, MapperException {
        return mapper.getString(repository.create(mapper.getCreatePassengerDto(passenger),user_id));
    }

    public String getByUserId(long user_id) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.readByUserId(user_id));
    }

    public String getBySNP(String passenger) throws JsonProcessingException, SQLException, MapperException {
        return mapper.getString(repository.readBySNP(mapper.getSearchPassengerDto(passenger)));
    }

    public String update(String passenger, long user_id) throws JsonProcessingException, SQLException, MapperException {
        return mapper.getString(repository.update(mapper.getUpdatePassengerDto(passenger), user_id));
    }

    public void delete(long passenger_id, long user_id) throws SQLException {
        repository.delete(passenger_id,user_id);
    }
}
