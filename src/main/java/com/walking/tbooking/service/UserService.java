package com.walking.tbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.dto.user.UpdateUserDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserService {
    private static UserService instance;

    private UserService(UserRepository repository){
        this.repository =repository;
    }
    public static UserService getInstance(UserRepository repository){
        if(instance==null){
            instance=new UserService(repository);
        }
        return instance;
    }

    private final UserRepository repository;

    public ReadUserDto create(CreateUserDto createUserDto, int roleId) throws SQLException, MapperException, JsonProcessingException {
        return repository.create(createUserDto,roleId);
    }

    public void updateLastEnter(long id) throws SQLException {
        repository.updateLastEnter(id);
    }

    public ReadUserDto updateData(UpdateUserDto updateUserDto) throws SQLException, MapperException {
        return repository.updateData(updateUserDto);
    }

    public void updatePassword(long id, String password) throws SQLException {
        repository.updatePassword(id,password);
    }

    public ReadUserDto readById(long id) throws SQLException, MapperException {
        return repository.readById(id);
    }

    public List<ReadUserDto> readAll() throws SQLException, MapperException {
        return repository.readAll();
    }

    public void ban(long id, boolean ban) throws SQLException {
        repository.ban(id,ban);
    }

    public void ban(Map<Long,Boolean> banList) throws SQLException {
        repository.ban(banList);
    }
}
