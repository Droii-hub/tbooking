package com.walking.tbooking.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.dto.ticket.TicketDto;

import java.util.List;

public class TicketJsonMapper {
    private final ObjectMapper mapper;

    public TicketJsonMapper(){
        this.mapper=new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public String getString(List<FullTicketDto> listFullTicketDto) throws JsonProcessingException{
        return mapper.writeValueAsString(listFullTicketDto);
    }

    public TicketDto getTicketDto(String json) throws JsonProcessingException{
        return mapper.readValue(json, TicketDto.class);
    }

    public String getString(FullTicketDto fullTicketDto) throws JsonProcessingException{
        return mapper.writeValueAsString(fullTicketDto);
    }
}
