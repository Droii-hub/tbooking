package com.walking.tbooking.controller;

import com.walking.tbooking.service.FlightService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/flight/seats")
public class SeatsServlet extends HttpServlet {
    private FlightService flightService;

    @Override
    public void init(){
        flightService=(FlightService) getServletContext().getAttribute("flightService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id=req.getParameter("id");
        if(id==null){
            resp.sendError(400);
            return;
        }
        var answer=flightService.getAvailableSeats(Long.parseLong(id));
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }
}
