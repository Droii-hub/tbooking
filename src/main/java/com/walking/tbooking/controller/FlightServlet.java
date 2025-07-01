package com.walking.tbooking.controller;

import com.walking.tbooking.dto.flight.ReadByAirportsFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.service.FlightService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/flight")
public class FlightServlet extends HttpServlet {
    private FlightService flightService;

    @Override
    public void init(){
        flightService=(FlightService) getServletContext().getAttribute("flightService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ReadByAirportsFlightDto readByAirportsFlightDto=(ReadByAirportsFlightDto) req.getAttribute("requestJavaObject");
        List<ReadFlightDto> answer;
        if(readByAirportsFlightDto==null)
            answer=flightService.getAll();
        else
            answer=flightService.getFlightByAirports(readByAirportsFlightDto);
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }
}
