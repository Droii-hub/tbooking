package com.walking.tbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.AirportJsonMapper;
import com.walking.tbooking.mapper.AirportMapper;
import com.walking.tbooking.repository.AirportRepository;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.List;

public class AirportService {
    private static AirportService instance;
    private AirportService(AirportRepository repository, AirportJsonMapper mapper){
        this.repository=repository;
        this.mapper=mapper;
    }
    public static AirportService getInstance(AirportRepository repository, AirportJsonMapper mapper){
        if(instance==null){
            instance=new AirportService(repository, mapper);
        }
        return instance;
    }

    private final AirportRepository repository;
    private final AirportJsonMapper mapper;

    public String create(String airport) throws JsonProcessingException, SQLException, MapperException {
        return mapper.getString(repository.create(mapper.getAirportDto(airport)));
    }

    public String getByName(String name) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.readByName(name));
    }

    public String getByLocation(String location) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.readByName(location));
    }

    public String getByIata(String iata) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.readByName(iata));
    }

    public String update(String airport) throws JsonProcessingException, SQLException, MapperException {
        return mapper.getString(repository.create(mapper.getAirportDto(airport)));
    }

    public void delete(String iata) throws SQLException {
        repository.delete(iata);
    }
}
