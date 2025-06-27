package com.walking.tbooking.controller;

import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.UpdatePassengerDto;
import com.walking.tbooking.service.PassengerService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/passenger")
public class PassengerCRUDServlet extends HttpServlet {
    private PassengerService passengerService;

    @Override
    public void init(){
        passengerService=(PassengerService) getServletContext().getAttribute("passengerService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var javaObject=req.getAttribute("requestJavaObject");
        HttpSession session=req.getSession(false);
        if (javaObject==null) {
            resp.sendError(400);
            return;
        }
        var answer=passengerService.create((CreatePassengerDto) javaObject,
                (long)session.getAttribute("userId"));
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session=req.getSession(false);
        var answer=passengerService.getByUserId((long)session.getAttribute("userId"));
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var javaObject=req.getAttribute("requestJavaObject");
        HttpSession session=req.getSession(false);
        if (javaObject==null) {
            resp.sendError(400);
            return;
        }
        UpdatePassengerDto updatePassengerDto=(UpdatePassengerDto) javaObject;
        long userId=(long)session.getAttribute("userId");
        try {
            var answer = passengerService.update(updatePassengerDto, userId);
            req.setAttribute("responseJavaObject", answer);
            resp.setStatus(200);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Passenger with this id is not exist")) {
                resp.sendError(400, e.getMessage());
            } else if (e.getMessage().equals("Passenger id does not match user id")) {
                resp.sendError(401, "The requested passenger belongs to another user");
            } else
                throw e;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String passengerId=req.getParameter("id");
        HttpSession session=req.getSession(false);
        if (passengerId==null) {
            resp.sendError(400);
            return;
        }
        long userId=(long)session.getAttribute("userId");
        try {
            passengerService.delete(Long.parseLong(passengerId), userId);
            resp.setStatus(200);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Passenger with this id is not exist")) {
                resp.sendError(400, e.getMessage());
            } else if (e.getMessage().equals("Passenger id does not match user id")) {
                resp.sendError(401, "The requested passenger belongs to another user");
            } else
                throw e;
        }
    }


}
