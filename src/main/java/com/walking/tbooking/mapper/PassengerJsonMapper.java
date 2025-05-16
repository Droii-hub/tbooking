package com.walking.tbooking.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.dto.passenger.SearchPassengerDto;
import com.walking.tbooking.dto.passenger.UpdatePassengerDto;

import java.util.List;

public class PassengerJsonMapper {
    private final ObjectMapper mapper;

    public PassengerJsonMapper(){
        this.mapper=new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public CreatePassengerDto getCreatePassengerDto(String json) throws JsonProcessingException {
        return mapper.readValue(json, CreatePassengerDto.class);
    }

    public String getString(ReadPassengerDto readPassengerDto) throws JsonProcessingException {
        return mapper.writeValueAsString(readPassengerDto);
    }

    public String getString(List<ReadPassengerDto> listReadPassengerDto) throws JsonProcessingException {
        return mapper.writeValueAsString(List.of(listReadPassengerDto));
    }

    public SearchPassengerDto getSearchPassengerDto(String json) throws JsonProcessingException {
        return mapper.readValue(json, SearchPassengerDto.class);
    }

    public UpdatePassengerDto getUpdatePassengerDto(String json) throws JsonProcessingException {
        return mapper.readValue(json, UpdatePassengerDto.class);
    }
}
