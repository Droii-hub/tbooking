package com.walking.tbooking.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.dto.user.UpdateUserDto;

import java.util.List;
import java.util.Map;

public class UserJsonMapper {
    private final ObjectMapper objectMapper;

    public UserJsonMapper(){
        this.objectMapper=new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public CreateUserDto getCreateUserDto(String json) throws JsonProcessingException {
        return  objectMapper.readValue(json, CreateUserDto.class);
    }

    public String getString(ReadUserDto readUserDto) throws JsonProcessingException{
        return objectMapper.writeValueAsString(readUserDto);
    }

    public UpdateUserDto getUpdateUserDto(String json) throws JsonProcessingException{
        return objectMapper.readValue(json, UpdateUserDto.class);
    }

    public String getString(List<ReadUserDto> listReadUserDto) throws JsonProcessingException{
        return objectMapper.writeValueAsString(List.of(listReadUserDto));
    }

    public Map<Long, Boolean> getMap(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<Map<Long, Boolean>>() {});
    }
}
