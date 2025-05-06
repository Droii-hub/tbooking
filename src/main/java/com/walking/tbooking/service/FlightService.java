package com.walking.tbooking.service;

import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadByAirportsFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;

import java.util.List;

public class FlightService {
    private static FlightService instance;
    private FlightService(){

    }
    public static FlightService getInstance(){
        if(instance==null){
            instance=new FlightService();
        }
        return instance;
    }

    public ReadFlightDto create(CreateFlightDto flight){
        throw new RuntimeException("Not implemented");
    }

    public List<Integer> getAvailableSeats(long id){
        throw new RuntimeException("Not implemented");
    }

    public List<ReadFlightDto> getFlightByAirports(ReadByAirportsFlightDto airports){
        throw new RuntimeException("Not implemented");
    }

    public List<ReadFlightDto> getAll(){
        throw new RuntimeException("Not implemented");
    }

    public ReadFlightDto update(ReadFlightDto flight){
        throw new RuntimeException("Not implemented");
    }

    public void delete(long id){
        throw new RuntimeException("Not implemented");
    }
}
