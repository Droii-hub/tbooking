package com.walking.tbooking.service;

import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.repository.AirportRepository;

import java.util.List;

public class AirportService {
    private static AirportService instance;
    private AirportService(AirportRepository repository){
        this.repository=repository;
    }
    public static AirportService getInstance(AirportRepository repository){
        if(instance==null){
            instance=new AirportService(repository);
        }
        return instance;
    }

    private final AirportRepository repository;

    public AirportDto create(AirportDto airport){
        return repository.create(airport);
    }

    public List<AirportDto> getByName(String name){
        return repository.readByName(name);
    }

    public List<AirportDto> getByLocation(String location){
        return repository.readByLocation(location);
    }

    public AirportDto getByIata(String iata){
        return repository.readByIata(iata);
    }

    public AirportDto update(AirportDto airport){
        return repository.update(airport);
    }

    public void delete(String iata){
        repository.delete(iata);
    }
}
