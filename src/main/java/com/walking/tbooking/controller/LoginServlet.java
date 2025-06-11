package com.walking.tbooking.controller;

import com.walking.tbooking.PasswordProvider;
import com.walking.tbooking.dto.user.LoginUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.exception.BadRequestException;
import com.walking.tbooking.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(){
        userService=(UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginUserDto loginUserDto=(LoginUserDto) request.getAttribute("requestJavaObject");
        try {
            ReadUserDto readUserDto = userService.readByEmail(loginUserDto.getEmail());
            if (!PasswordProvider.checkPassword(loginUserDto.getPassword(), userService.passwordById(readUserDto.getId())))
                response.sendError(401);
            HttpSession session=request.getSession();
            session.setAttribute("userId",readUserDto.getId());
            session.setAttribute("roleId", readUserDto.getRoleId());
            userService.updateLastEnter(readUserDto.getId());
            response.setStatus(200);
        } catch (BadRequestException e) {
            response.sendError(401, e.getMessage());
        }

    }
}
