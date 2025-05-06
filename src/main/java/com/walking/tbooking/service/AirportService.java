package com.walking.tbooking.service;

import com.walking.tbooking.dto.airport.AirportDto;

import java.util.List;

public class AirportService {
    private static AirportService instance;
    private AirportService(){

    }
    public static AirportService getInstance(){
        if(instance==null){
            instance=new AirportService();
        }
        return instance;
    }

    public AirportDto create(AirportDto airport){
        throw new RuntimeException("Not implemented");
    }

    public List<AirportDto> getByName(String name){
        throw new RuntimeException("Not implemented");
    }

    public List<AirportDto> getByLocation(String location){
        throw new RuntimeException("Not implemented");
    }

    public AirportDto getByIata(String iata){
        throw new RuntimeException("Not implemented");
    }

    public AirportDto update(AirportDto airport){
        throw new RuntimeException("Not implemented");
    }

    public void delete(String iata){
        throw new RuntimeException("Not implemented");
    }
}
