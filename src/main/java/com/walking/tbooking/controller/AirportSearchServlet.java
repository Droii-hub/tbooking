package com.walking.tbooking.controller;

import com.walking.tbooking.service.AirportService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/airport")
public class AirportSearchServlet extends HttpServlet {
    private AirportService airportService;

    @Override
    public void init(){
        airportService=(AirportService) getServletContext().getAttribute("airportService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name=req.getParameter("name");
        String location=req.getParameter("location");
        String iata=req.getParameter("iata");
        if(name==null&&location==null&&iata==null){
            resp.sendError(400);
            return;
        }

        if(iata!=null){
            req.setAttribute("responseJavaObject", airportService.getByIata(iata));
            resp.setStatus(200);
            return;
        }

        if(location!=null){
            req.setAttribute("responseJavaObject", airportService.getByLocation(location));
            resp.setStatus(200);
            return;
        }

        req.setAttribute("responseJavaObject", airportService.getByName(name));
        resp.setStatus(200);
    }
}
