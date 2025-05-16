package com.walking.tbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.walking.tbooking.mapper.UserJsonMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class UserServiceTest {
    @Test
    void mapSerialization() throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        HashMap<Long, Boolean> banList=new HashMap<>();
        banList.put(1L,true);
        banList.put(2L,false);
        banList.put(3L,true);
        var json=objectMapper.writeValueAsString(banList);
        System.out.println(json);
        UserJsonMapper mapper=new UserJsonMapper();
        var actual=mapper.getMap(json);
        Assertions.assertTrue(actual.get(1L));
        Assertions.assertFalse(actual.get(2L));
        Assertions.assertTrue(actual.get(3L));
    }
}
