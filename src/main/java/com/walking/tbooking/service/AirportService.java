package com.walking.tbooking.service;

import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.repository.AirportRepository;

import java.sql.SQLException;
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

    public AirportDto create(AirportDto airport) throws SQLException, MapperException {
        return repository.create(airport);
    }

    public List<AirportDto> getByName(String name) throws SQLException, MapperException {
        return repository.readByName(name);
    }

    public List<AirportDto> getByLocation(String location) throws SQLException, MapperException {
        return repository.readByLocation(location);
    }

    public AirportDto getByIata(String iata) throws SQLException, MapperException {
        return repository.readByIata(iata);
    }

    public AirportDto update(AirportDto airport) throws SQLException, MapperException {
        return repository.update(airport);
    }

    public void delete(String iata) throws SQLException {
        repository.delete(iata);
    }
}
