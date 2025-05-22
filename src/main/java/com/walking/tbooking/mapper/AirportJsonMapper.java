package com.walking.tbooking.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.walking.tbooking.dto.airport.AirportDto;

import java.util.List;

public class AirportJsonMapper {
    private final ObjectMapper mapper;

    public AirportJsonMapper(){
        this.mapper=new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public AirportDto getAirportDto(String json) throws JsonProcessingException {
        return mapper.readValue(json, AirportDto.class);
    }

    public String getString(AirportDto airportDto) throws JsonProcessingException{
        return mapper.writeValueAsString(airportDto);
    }

    public String getString(List<AirportDto> listAirportDto)throws JsonProcessingException{
        return mapper.writeValueAsString(List.of(listAirportDto));
    }
}
