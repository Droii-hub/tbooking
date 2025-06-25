package com.walking.tbooking.controller;

import com.walking.tbooking.dto.user.UpdateUserDto;
import com.walking.tbooking.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(){
        userService=(UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UpdateUserDto updateUserDto=(UpdateUserDto) req.getAttribute("requestJavaObject");
        if (updateUserDto==null)
            resp.sendError(400, "Empty request");
        HttpSession session=req.getSession(false);
        var answer=userService.updateData((long)session.getAttribute("userId"), updateUserDto);
        req.setAttribute("responseJavaObject",answer);
        resp.setStatus(200);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id=req.getParameter("id");
        String newPassword=req.getParameter("password");
        if (id==null&newPassword==null)
            resp.sendError(400, "Empty parameters");
        userService.updatePassword(Long.parseLong(id), newPassword);
        resp.setStatus(200);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        var answer=userService.readAll();
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }
}
