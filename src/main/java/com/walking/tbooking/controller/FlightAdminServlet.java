package com.walking.tbooking.controller;

import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.service.FlightService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/flight/admin")
public class FlightAdminServlet extends HttpServlet {
    private FlightService flightService;

    @Override
    public void init(){
        flightService=(FlightService) getServletContext().getAttribute("flightService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateFlightDto createFlightDto=(CreateFlightDto) req.getAttribute("requestJavaObject");
        if(createFlightDto==null){
            resp.sendError(400);
            return;
        }
        var answer=flightService.create(createFlightDto);
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ReadFlightDto readFlightDto=(ReadFlightDto) req.getAttribute("requestJavaObject");
        if(readFlightDto==null){
            resp.sendError(400);
            return;
        }
        var answer=flightService.update(readFlightDto);
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id=req.getParameter("id");
        if(id==null){
            resp.sendError(400);
            return;
        }
        flightService.delete(Long.parseLong(id));
        resp.setStatus(200);
    }
}
