package com.walking.tbooking.controller;

import com.walking.tbooking.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/user/ban")
public class UserBanServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(){
        userService=(UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp){
        var javaObject=req.getAttribute("requestJavaObject");
        String id=req.getParameter("id");
        String ban=req.getParameter("ban");
        if (javaObject!=null){
            userService.ban((Map<Long, Boolean>) javaObject);
            resp.setStatus(200);
        } else if(id!=null&ban!=null){
            userService.ban(Long.parseLong(id), Boolean.parseBoolean(ban));
            resp.setStatus(200);
        } else{
            resp.setStatus(400);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            this.doPatch(req, resp);
        }
    }
}
