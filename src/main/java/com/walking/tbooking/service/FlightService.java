package com.walking.tbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadByAirportsFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.dto.flight.SeatsDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.FlightJsonMapper;
import com.walking.tbooking.mapper.FlightMapper;
import com.walking.tbooking.repository.FlightRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FlightService {
    private static FlightService instance;
    private FlightService(FlightRepository repository, FlightJsonMapper mapper){
        this.repository=repository;
        this.mapper=mapper;
    }
    public static FlightService getInstance(FlightRepository repository, FlightJsonMapper mapper){
        if(instance==null){
            instance=new FlightService(repository, mapper);
        }
        return instance;
    }

    private final FlightRepository repository;
    private final FlightJsonMapper mapper;

    public String create(String flight) throws JsonProcessingException, SQLException, MapperException {
        return mapper.getString(repository.create(mapper.getCreateFlightDto(flight)));
    }

    public String getAvailableSeats(long id) throws SQLException, JsonProcessingException {
        SeatsDto seats=repository.readSeats(id);
        LinkedList<Integer> unavailableSeats=new LinkedList<>(seats.getUnavailableSeats());
        List<Boolean> availableSeats=new ArrayList<>(seats.getTotalSeats());
        for (int i = 0; i < seats.getTotalSeats(); i++) {
            if (i==unavailableSeats.getFirst()-1){
                unavailableSeats.removeFirst();
                availableSeats.add(i,false);
            } else {
                availableSeats.add(i, true);
            }
        }
        return mapper.getString(availableSeats);
    }

    public String getFlightByAirports(String airports) throws JsonProcessingException, SQLException, MapperException {
        return mapper.getStringByListReadFlightDto(
                repository.readByAirports(mapper.getReadByAirportsFlightDto(airports)));
    }

    public String getAll() throws SQLException, MapperException, JsonProcessingException {
        return mapper.getStringByListReadFlightDto(repository.readAll());
    }

    public String update(String flight) throws JsonProcessingException, SQLException, MapperException {
        return mapper.getString(repository.update(mapper.getReadFlightDto(flight)));
    }

    public void delete(long id) throws SQLException {
        repository.delete(id);
    }
}
