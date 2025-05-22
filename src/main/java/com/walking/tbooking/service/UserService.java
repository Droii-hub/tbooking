package com.walking.tbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.UserJsonMapper;
import com.walking.tbooking.repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    private static UserService instance;

    private UserService(UserRepository repository, UserJsonMapper mapper){
        this.repository =repository;
        this.mapper=mapper;
    }
    public static UserService getInstance(UserRepository repository, UserJsonMapper mapper){
        if(instance==null){
            instance=new UserService(repository, mapper);
        }
        return instance;
    }

    private final UserRepository repository;
    private final UserJsonMapper mapper;

    public String create(String json, int roleId) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.create(mapper.getCreateUserDto(json), roleId));
    }

    public void updateLastEnter(long id) throws SQLException {
        repository.updateLastEnter(id);
    }

    public String updateData(String json) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.updateData(mapper.getUpdateUserDto(json)));
    }

    public void updatePassword(long id, String password) throws SQLException {
        repository.updatePassword(id,password);
    }

    public String readById(long id) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.readById(id));
    }

    public String readAll() throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(repository.readAll());
    }

    public void ban(long id, boolean ban) throws SQLException {
        repository.ban(id,ban);
    }

    public void ban(String json) throws SQLException, JsonProcessingException{
        repository.ban(mapper.getMap(json));
    }
}
