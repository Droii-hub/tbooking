package com.walking.tbooking.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.walking.tbooking.mapper.*;
import com.walking.tbooking.repository.*;
import com.walking.tbooking.service.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InitParamsListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event){
        ServletContext context=event.getServletContext();
        HikariConfig config=new HikariConfig(getHikariProperties(context));
        HikariDataSource dataSource=new HikariDataSource(config);
        FluentConfiguration fluentConfiguration= Flyway.configure()
                .dataSource(dataSource).baselineOnMigrate(true);
        Flyway flyway=fluentConfiguration.load();
        flyway.migrate();
        context.setAttribute("dbConnection", dataSource);
        context.setAttribute("userService", UserService.getInstance(
           new UserRepository(dataSource, new UserMapper())
        ));

        context.setAttribute("airportService", AirportService.getInstance(
                new AirportRepository(dataSource, new AirportMapper())
        ));
        TicketRepository ticketRepository=new TicketRepository(dataSource, new TicketMapper());
        FlightRepository flightRepository=new FlightRepository(dataSource, new FlightMapper());
        PassengerRepository passengerRepository=new PassengerRepository(dataSource, new PassengerMapper());
        context.setAttribute("passengerService", PassengerService.getInstance(passengerRepository));
        context.setAttribute("flightService", FlightService.getInstance(
                flightRepository
        ));
        context.setAttribute("ticketService", TicketService.getInstance(
                ticketRepository
        ));
        context.setAttribute("bookingService", BookingService.getInstance(
                dataSource,
                ticketRepository,
                flightRepository,
                passengerRepository,
                new FavoriteAirportsRepository(dataSource)
        ));
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        context.setAttribute("objectMapper", objectMapper);
    }

    private Properties getHikariProperties(ServletContext context){
        String HIKARI_PATH = "/WEB-INF/hikari.properties";
        try(InputStream inputStream=context.getResourceAsStream(HIKARI_PATH)) {
            if (inputStream==null){
                throw new RuntimeException("Can't find config file: "+ HIKARI_PATH);
            }
            Properties properties=new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event){
        var context=event.getServletContext();
        HikariDataSource connection=(HikariDataSource)context.getAttribute("dbConnection");
        connection.close();
    }
}
