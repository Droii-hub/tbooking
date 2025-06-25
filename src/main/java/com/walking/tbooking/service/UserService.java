package com.walking.tbooking.service;

import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.dto.user.UpdateUserDto;
import com.walking.tbooking.repository.UserRepository;

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

    public ReadUserDto create(CreateUserDto createUserDto, int roleId){
        return repository.create(createUserDto,roleId);
    }

    public void updateLastEnter(long id){
        repository.updateLastEnter(id);
    }

    public ReadUserDto updateData(long id, UpdateUserDto updateUserDto){
        return repository.updateData(id, updateUserDto);
    }

    public void updatePassword(long id, String password){
        repository.updatePassword(id,password);
    }

    public ReadUserDto readByEmail(String email){
        return repository.readByEmail(email);
    }

    public String passwordById(long id){
        return repository.passwordById(id);
    }

    public List<ReadUserDto> readAll(){
        return repository.readAll();
    }

    public void ban(long id, boolean ban){
        repository.ban(id,ban);
    }

    public void ban(Map<Long,Boolean> banList){
        repository.ban(banList);
    }
}
