package com.walking.tbooking.service;

import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.dto.user.UpdateUserDto;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class UserService {
    private static UserService instance;
    private UserService(DataSource dataSource){

    }
    public static UserService getInstance(DataSource dataSource){
        if(instance==null){
            instance=new UserService(dataSource);
        }
        return instance;
    }

    public ReadUserDto create(CreateUserDto createUserDto, int roleId){
        throw new RuntimeException("Not implemented");
    }

    public void updateLastEnter(long id){
        throw new RuntimeException("Not implemented");
    }

    public ReadUserDto updateData(UpdateUserDto updateUserDto){
        throw new RuntimeException("Not implemented");
    }

    public void updatePassword(long id, String password){
        throw new RuntimeException("Not implemented");
    }

    public ReadUserDto read(long id){
        throw new RuntimeException("Not implemented");
    }

    public List<ReadUserDto> readAll(){
        throw new RuntimeException("Not implemented");
    }

    public void ban(long id, boolean ban){
        throw new RuntimeException("Not implemented");
    }

    public void ban(Map<Long,Boolean> banList){
        throw new RuntimeException("Not implemented");
    }
}
