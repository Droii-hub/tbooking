package com.walking.tbooking.controller;

import com.walking.tbooking.dto.passenger.SearchPassengerDto;
import com.walking.tbooking.service.PassengerService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/passenger/search")
public class PassengerSearchServlet extends HttpServlet {
    private PassengerService passengerService;

    @Override
    public void init(){
        passengerService=(PassengerService) getServletContext().getAttribute("passengerService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var javaObject=req.getAttribute("requestJavaObject");
        if (javaObject==null) {
            resp.sendError(400);
            return;
        }
        var answer=passengerService.getBySNP((SearchPassengerDto) javaObject);
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }
}
