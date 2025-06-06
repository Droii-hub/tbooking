package com.walking.tbooking.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadByAirportsFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.SearchPassengerDto;
import com.walking.tbooking.dto.passenger.UpdatePassengerDto;
import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.UpdateUserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeserializationFilter extends HttpFilter {
    private ObjectMapper mapper;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        if (mapper==null)
            mapper=(ObjectMapper) getFilterConfig().getServletContext().getAttribute("objectMapper");

        if (!"application/json".equals(req.getContentType()) || req.getContentLength() == 0) {
            chain.doFilter(req, res);
            return;
        }
        req.setAttribute("requestJavaObject", deserialize(req));
        chain.doFilter(req, res);
    }

    private Object deserialize(HttpServletRequest req) throws IOException {
        byte[] jsonBody = req.getInputStream().readAllBytes();
        var classMap=getClassMap();
        TypeReference<?> targetType =classMap.get(req.getServletPath()+req.getMethod());
        return mapper.readValue(jsonBody, targetType);
    }

    private HashMap<String, TypeReference<?>> getClassMap() {
        HashMap<String, TypeReference<?>> result=new HashMap<>();
        result.put("/userPOST", new TypeReference<CreateUserDto>() {});
        result.put("/userPUT", new TypeReference<UpdateUserDto>(){});
        result.put("/user/ban/POST", new TypeReference<Map<Long, Boolean>>(){});
        result.put("/passengerPOST", new TypeReference<CreatePassengerDto>(){});
        result.put("/passengerGET", new TypeReference<SearchPassengerDto>(){});
        result.put("/passengerPUT", new TypeReference<UpdatePassengerDto>(){});
        result.put("/airportPOST", new TypeReference<AirportDto>(){});
        result.put("/airportPUT", new TypeReference<AirportDto>(){});
        result.put("/flightPOST", new TypeReference<CreateFlightDto>() {});
        result.put("/flightGET", new TypeReference<ReadByAirportsFlightDto>() {});
        result.put("/flightPUT", new TypeReference<ReadFlightDto>() {});
        result.put("/ticketPOST", new TypeReference<TicketDto>(){});
        return result;
    }

}
