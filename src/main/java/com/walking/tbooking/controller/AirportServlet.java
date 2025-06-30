package com.walking.tbooking.controller;

import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.service.AirportService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/airport/admin")
public class AirportServlet extends HttpServlet {
    private AirportService airportService;

    @Override
    public void init(){
        airportService=(AirportService) getServletContext().getAttribute("airportService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AirportDto airportDto=(AirportDto) req.getAttribute("requestJavaObject");
        if (airportDto==null){
            resp.sendError(400);
            return;
        }

        var answer=airportService.create(airportDto);
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AirportDto airportDto=(AirportDto) req.getAttribute("requestJavaObject");
        if (airportDto==null){
            resp.sendError(400);
            return;
        }

        var answer=airportService.update(airportDto);
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String iata=req.getParameter("iata");
        if(iata!=null){
            airportService.delete(iata);
            resp.setStatus(200);
        } else
            resp.sendError(400);
    }
}
