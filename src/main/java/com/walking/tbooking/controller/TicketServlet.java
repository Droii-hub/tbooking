package com.walking.tbooking.controller;

import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.service.BookingService;
import com.walking.tbooking.service.TicketService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/ticket")
public class TicketServlet extends HttpServlet {
    private TicketService ticketService;
    private BookingService bookingService;

    @Override
    public void init(){
        ticketService=(TicketService) getServletContext().getAttribute("ticketService");
        bookingService=(BookingService) getServletContext().getAttribute("bookingService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TicketDto ticketDto=(TicketDto) req.getAttribute("requestJavaObject");
        HttpSession httpSession=req.getSession(false);
        if(ticketDto==null){
            resp.sendError(400);
            return;
        }
        try {
            var answer = bookingService.book((long) httpSession.getAttribute("userId"), ticketDto);
            req.setAttribute("responseJavaObject", answer);
            resp.setStatus(200);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Unauthorized"))
                resp.sendError(401);
            else throw e;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String flight_id = req.getParameter("flight_id");
        String seat = req.getParameter("seat");
        HttpSession httpSession = req.getSession(false);
        if (flight_id == null | seat == null) {
            resp.sendError(400);
            return;
        }
        try {
            bookingService.unbook((long)httpSession.getAttribute("userId"),
                    (int)httpSession.getAttribute("roleId"),
                    Long.parseLong(flight_id),
                    Integer.parseInt(seat));
        }  catch (RuntimeException e) {
            if (e.getMessage().equals("Unauthorized"))
                resp.sendError(401);
            else throw e;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        HttpSession httpSession = req.getSession(false);
        long userId=(long)httpSession.getAttribute("userId");
        int roleId=(int)httpSession.getAttribute("roleId");
        String actual=req.getParameter("actual");
        List<FullTicketDto> answer;
        if(roleId==1){
            answer=ticketService.getAll();
            req.setAttribute("responseJavaObject", answer);
        } else if (actual!=null&Boolean.getBoolean(actual)){
            answer=ticketService.getActualByUser(userId);

        } else {
            answer=ticketService.getAllByUser(userId);
        }
        req.setAttribute("responseJavaObject", answer);
        resp.setStatus(200);
    }
}
