package com.walking.tbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.dto.user.UpdateUserDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.UserJsonMapper;
import com.walking.tbooking.mapper.UserMapper;
import com.walking.tbooking.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserService {
    private static UserService instance;
    private final UserRepository userRepository;
    private final UserJsonMapper mapper;
    private UserService(DataSource dataSource){
        this.userRepository=new UserRepository(dataSource, new UserMapper());
        this.mapper=new UserJsonMapper();
    }
    public static UserService getInstance(DataSource dataSource){
        if(instance==null){
            instance=new UserService(dataSource);
        }
        return instance;
    }

    public String create(String json, int roleId) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(userRepository.create(mapper.getCreateUserDto(json), roleId));
    }

    public void updateLastEnter(long id) throws SQLException {
        userRepository.updateLastEnter(id);
    }

    public String updateData(String json) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(userRepository.updateData(mapper.getUpdateUserDto(json)));
    }

    public void updatePassword(long id, String password) throws SQLException {
        userRepository.updatePassword(id,password);
    }

    public String readById(long id) throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(userRepository.readById(id));
    }

    public String readAll() throws SQLException, MapperException, JsonProcessingException {
        return mapper.getString(userRepository.readAll());
    }

    public void ban(long id, boolean ban) throws SQLException {
        userRepository.ban(id,ban);
    }

    public void ban(String json) throws SQLException, JsonProcessingException{
        userRepository.ban(mapper.getMap(json));
    }
}
