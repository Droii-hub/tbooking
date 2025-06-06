package com.walking.tbooking.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeserializationFilterTest {
    @Spy
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse resp;

    @Mock
    private FilterChain chain;

    @InjectMocks
    private DeserializationFilter dFilter;

    @Test
    void createUserDto_success() throws IOException, ServletException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        CreateUserDto createUserDto=new CreateUserDto();
        createUserDto.setEmail("test@email.com");
        createUserDto.setSurname("Petrov");
        createUserDto.setName("Ivan");
        createUserDto.setPatronymic("Aleksandrovich");
        createUserDto.setPassword("GreatPassword");
        ObjectMapper mapper=new ObjectMapper();
//        doReturn("application/json").when(req).getContentType();
//        doReturn(5).when(req).getContentLength();
        doReturn("/user").when(req).getServletPath();
        doReturn("POST").when(req).getMethod();
        byte[] body=mapper.writeValueAsBytes(createUserDto);
        ServletInputStream sis= mock(ServletInputStream.class);
        doReturn(body).when(sis).readAllBytes();
        doReturn(sis).when(req).getInputStream();
        Method method=DeserializationFilter.class.getDeclaredMethod("deserialize", HttpServletRequest.class);
        method.setAccessible(true);

        //when
        var temp=(CreateUserDto)method.invoke(dFilter,req);
        var actual=temp.getEmail();
        //then
        assertEquals(createUserDto.getEmail(), actual);
    }

    @Test
    void test() throws JsonProcessingException {
        //given
        LinkedList<ReadUserDto> list=new LinkedList<>();
        list.add(new ReadUserDto());
        list.getFirst().setId(1L);
        list.getFirst().setEmail("test@email.com");
        list.getFirst().setName("Ivan");
        list.getFirst().setSurname("Ivanov");
        list.add(new ReadUserDto());
        list.getLast().setId(2L);
        list.getLast().setEmail("test2@email.com");
        list.getLast().setName("Petr");
        list.getLast().setSurname("Petrov");
        ObjectMapper mapper=new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Object object=list;
        String s=mapper.writeValueAsString(object);
        String s1=mapper.writeValueAsString(list);
        assertEquals(s,s1);
    }
}
