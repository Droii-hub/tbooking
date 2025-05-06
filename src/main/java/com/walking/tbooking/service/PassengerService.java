package com.walking.tbooking.service;

import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.dto.passenger.SearchPassengerDto;
import com.walking.tbooking.dto.passenger.UpdatePassengerDto;

import javax.sql.DataSource;
import java.util.List;

public class PassengerService {
    private static PassengerService instance;

    private PassengerService(DataSource dataSource){
        this.dataSource=dataSource;
    }

    public static PassengerService getInstance(DataSource dataSource){
        if(instance==null){
            instance=new PassengerService(dataSource);
        }
        return instance;
    }

    private final DataSource dataSource;

    public ReadPassengerDto create(CreatePassengerDto passenger, long user_id){
        throw new RuntimeException("Not implemented");
    }

    public List<ReadPassengerDto> getByUserId(long user_id){
        throw new RuntimeException("Not implemented");
    }

    public List<ReadPassengerDto> getBySNP(SearchPassengerDto passenger){
        throw new RuntimeException("Not implemented");
    }

    public ReadPassengerDto update(UpdatePassengerDto passenger, long user_id){
        throw new RuntimeException("Not implemented");
    }

    public void delete(long passenger_id, long user_id){
        throw new RuntimeException("Not implemented");
    }
}
