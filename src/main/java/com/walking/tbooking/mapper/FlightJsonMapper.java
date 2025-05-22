package com.walking.tbooking.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadByAirportsFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.dto.flight.SeatsDto;

import java.util.List;

public class FlightJsonMapper {
    private final ObjectMapper mapper;

    public FlightJsonMapper(){
        mapper=new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public CreateFlightDto getCreateFlightDto(String json) throws JsonProcessingException{
        return mapper.readValue(json, CreateFlightDto.class);
    }

    public String getString(ReadFlightDto readFlightDto) throws JsonProcessingException{
        return mapper.writeValueAsString(readFlightDto);
    }

    public String getString(List<Boolean> listBoolean) throws JsonProcessingException{
        return mapper.writeValueAsString(List.of(listBoolean));
    }

    public ReadByAirportsFlightDto getReadByAirportsFlightDto(String json) throws JsonProcessingException{
        return mapper.readValue(json, ReadByAirportsFlightDto.class);
    }

    public String getStringByListReadFlightDto(List<ReadFlightDto> listReadFlightDto) throws JsonProcessingException{
        return mapper.writeValueAsString(List.of(listReadFlightDto));
    }

    public ReadFlightDto getReadFlightDto(String json) throws JsonProcessingException{
        return mapper.readValue(json, ReadFlightDto.class);
    }

//    public String getString(SeatsDto seatsDto) throws JsonProcessingException{
//        return mapper.writeValueAsString(seatsDto);
//    }
}
