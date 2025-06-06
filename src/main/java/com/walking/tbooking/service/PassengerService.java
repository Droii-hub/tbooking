package com.walking.tbooking.service;

import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.dto.passenger.SearchPassengerDto;
import com.walking.tbooking.dto.passenger.UpdatePassengerDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.repository.PassengerRepository;

import java.sql.SQLException;
import java.util.List;

public class PassengerService {
    private static PassengerService instance;

    private PassengerService(PassengerRepository repository){
        this.repository=repository;
    }

    public static PassengerService getInstance(PassengerRepository repository){
        if(instance==null){
            instance=new PassengerService(repository);
        }
        return instance;
    }

    private final PassengerRepository repository;

    public ReadPassengerDto create(CreatePassengerDto createPassengerDto, long user_id) throws SQLException, MapperException {
        return repository.create(createPassengerDto, user_id);
    }

    public List<ReadPassengerDto> getByUserId(long user_id) throws SQLException, MapperException {
        return repository.readByUserId(user_id);
    }

    public List<ReadPassengerDto> getBySNP(SearchPassengerDto passenger) throws SQLException, MapperException {
        return repository.readBySNP(passenger);
    }

    public ReadPassengerDto update(UpdatePassengerDto passenger, long user_id) throws SQLException, MapperException {
        return repository.update(passenger, user_id);
    }

    public void delete(long passenger_id, long user_id) throws SQLException {
        repository.delete(passenger_id,user_id);
    }
}
