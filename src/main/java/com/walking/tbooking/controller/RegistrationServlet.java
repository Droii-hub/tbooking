package com.walking.tbooking.controller;

import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.exception.BadRequestException;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(){
        userService=(UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateUserDto createUserDto=(CreateUserDto) req.getAttribute("requestJavaObject");
        try {
            ReadUserDto answer = userService.create(createUserDto, (int) req.getAttribute("roleId"));
            resp.setStatus(200);
            req.setAttribute("responseJavaObject", answer);
        } catch (BadRequestException e){
            resp.sendError(400, "The server could not process the request with the current parameters");
        }
    }
}
