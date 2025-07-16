package com.walking.tbooking.service;

import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadByAirportsFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.dto.flight.SeatsDto;
import com.walking.tbooking.repository.FlightRepository;

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

    public ReadFlightDto create(CreateFlightDto flight){
        return repository.create(flight);
    }

    public List<Boolean> getAvailableSeats(long id){
        SeatsDto seats=repository.readSeats(id);
        LinkedList<Integer> unavailableSeats=new LinkedList<>(seats.getUnavailableSeats());
        List<Boolean> availableSeats=new ArrayList<>(seats.getTotalSeats());
        if (unavailableSeats.isEmpty()){
            for (int i = 0; i < seats.getTotalSeats(); i++) {
                availableSeats.add(i, true);
            }
        } else
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

    public List<ReadFlightDto> getFlightByAirports(ReadByAirportsFlightDto airports){
        return repository.readByAirports(airports);
    }

    public List<ReadFlightDto> getAll(){
        return repository.readAll();
    }

    public ReadFlightDto update(ReadFlightDto flight){
        return repository.update(flight);
    }

    public void delete(long id){
        repository.delete(id);
    }
}
