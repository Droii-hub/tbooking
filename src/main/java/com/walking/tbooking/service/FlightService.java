package com.walking.tbooking.service;

import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadByAirportsFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.dto.flight.SeatsDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.repository.FlightRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FlightService {
    private static FlightService instance;
    private FlightService(FlightRepository repository){
        this.repository=repository;
    }
    public static FlightService getInstance(FlightRepository repository){
        if(instance==null){
            instance=new FlightService(repository);
        }
        return instance;
    }

    private final FlightRepository repository;

    public ReadFlightDto create(CreateFlightDto flight) throws SQLException, MapperException {
        return repository.create(flight);
    }

    public List<Boolean> getAvailableSeats(long id) throws SQLException {
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
        return availableSeats;
    }

    public List<ReadFlightDto> getFlightByAirports(ReadByAirportsFlightDto airports) throws SQLException, MapperException {
        return repository.readByAirports(airports);
    }

    public List<ReadFlightDto> getAll() throws SQLException, MapperException {
        return repository.readAll();
    }

    public ReadFlightDto update(ReadFlightDto flight) throws SQLException, MapperException {
        return repository.update(flight);
    }

    public void delete(long id) throws SQLException {
        repository.delete(id);
    }
}
