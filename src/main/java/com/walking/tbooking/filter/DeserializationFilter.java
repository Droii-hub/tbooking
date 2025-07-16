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
import com.walking.tbooking.dto.user.LoginUserDto;
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
    public void init(){
        mapper=(ObjectMapper) getFilterConfig().getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
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
        result.put("/registrationPOST", new TypeReference<CreateUserDto>() {});
        result.put("/loginPOST", new TypeReference<LoginUserDto>() {});
        result.put("/userPUT", new TypeReference<UpdateUserDto>(){});
        result.put("/userPOST", new TypeReference<Map<String,String>>() {});
        result.put("/user/ban/PATCH", new TypeReference<Map<Long, Boolean>>(){});
        result.put("/passengerPOST", new TypeReference<CreatePassengerDto>(){});
        result.put("/passengerPUT", new TypeReference<UpdatePassengerDto>(){});
        result.put("/passenger/searchGET", new TypeReference<SearchPassengerDto>() {});
        result.put("/airport/adminPOST", new TypeReference<AirportDto>(){});
        result.put("/airport/adminPUT", new TypeReference<AirportDto>(){});
        result.put("/flight/adminPOST", new TypeReference<CreateFlightDto>() {});
        result.put("/flightGET", new TypeReference<ReadByAirportsFlightDto>() {});
        result.put("/flight/adminPUT", new TypeReference<ReadFlightDto>() {});
        result.put("/ticketPOST", new TypeReference<TicketDto>(){});
        return result;
    }

}
