package com.walking.tbooking.repository;

import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TicketRepositoryTest {
    private HikariDataSource dataSource;
    private TicketRepository ticketRepository;
    private TicketDto ticketDto;
    private ReadFlightDto actualFlight;
    private ReadUserDto user;
    private ReadPassengerDto passenger;
    private TicketDto actualTicketDto;
    private TicketDto anotherUserTicketDto;

    @BeforeEach
    void setUp() throws SQLException, MapperException{
        HikariConfig config = new HikariConfig("hikari_test.properties");
        dataSource = new HikariDataSource(config);
        FluentConfiguration flywayConfig = Flyway.configure()
                .locations("filesystem:" + System.getProperty("user.dir") + "/src/main/resources/db/migration")
                .dataSource(dataSource);
        Flyway flyway = flywayConfig.load();
        flyway.migrate();

        AirportRepository airportRepository=new AirportRepository(dataSource, new AirportMapper());
        AirportDto airportDto=new AirportDto();
        airportDto.setIata("OMS");
        airportDto.setName("Tsentralny");
        airportDto.setLocation("Omsk");
        airportRepository.create(airportDto);
        airportDto.setIata("OME");
        airportDto.setName("Nome");
        airportDto.setLocation("Alaska");
        airportRepository.create(airportDto);

        FlightRepository flightRepository = new FlightRepository(dataSource, new FlightMapper());
        CreateFlightDto createFlightDto=new CreateFlightDto();
        createFlightDto.setDeparture_airport("OMS");
        createFlightDto.setDeparture_time(LocalDateTime.of(2025,5,13, 18,0,0));
        createFlightDto.setArrival_airport("OME");
        createFlightDto.setArrival_time(LocalDateTime.of(2025,5,13,23,0,0));
        createFlightDto.setTotal_seats(120);
        ReadFlightDto flight = flightRepository.create(createFlightDto);
        CreateFlightDto actualFlightDto=new CreateFlightDto();
        actualFlightDto.setDeparture_airport("OME");
        actualFlightDto.setArrival_airport("OMS");
        actualFlightDto.setDeparture_time(LocalDateTime.now().plusDays(1));
        actualFlightDto.setArrival_time(actualFlightDto.getDeparture_time().plusHours(5));
        actualFlightDto.setTotal_seats(40);
        actualFlight= flightRepository.create(actualFlightDto);

        UserRepository userRepository = new UserRepository(dataSource, new UserMapper());
        CreateUserDto userDto=new CreateUserDto();
        userDto.setEmail("test@email.com");
        userDto.setSurname("Petrov");
        userDto.setName("Ivan");
        userDto.setPatronymic("Aleksandrovich");
        userDto.setPassword("GreatPassword");
        user=userRepository.create(userDto, 2);
        userDto.setEmail("test2@email.com");
        userDto.setSurname("Ivanov");
        ReadUserDto anotherUser = userRepository.create(userDto, 2);


        PassengerRepository passengerRepository=new PassengerRepository(dataSource,new PassengerMapper());
        CreatePassengerDto passengerDto=new CreatePassengerDto();
        passengerDto.setSurname("Petrov");
        passengerDto.setName("Ivan");
        passengerDto.setPatronymic("Aleksandrovich");
        passengerDto.setMale(true);
        passengerDto.setBirth_date(LocalDate.of(1986,4,15));
        passengerDto.setPassport_series(9438);
        passengerDto.setPassport_number(876543);
        passengerDto.setPassport_source("UFMS g Arzamas");
        passengerDto.setPassport_issue_date(LocalDate.of(2002,4,27));
        passenger=passengerRepository.create(passengerDto,user.getId());
        passengerDto.setSurname("Ivanov");
        ReadPassengerDto anotherPassenger = passengerRepository.create(passengerDto, anotherUser.getId());


        ticketDto=new TicketDto();
        ticketDto.setPassenger_id(passenger.getId());
        ticketDto.setService_class_id(2);
        ticketDto.setBaggage_allowance("20 kg");
        ticketDto.setFlight_id(flight.getId());
        ticketDto.setSeat(5);

        actualTicketDto=new TicketDto();
        actualTicketDto.setFlight_id(actualFlight.getId());
        actualTicketDto.setSeat(3);
        actualTicketDto.setService_class_id(3);
        actualTicketDto.setPassenger_id(passenger.getId());
        actualTicketDto.setBaggage_allowance("10 kg");

        anotherUserTicketDto=new TicketDto();
        anotherUserTicketDto.setFlight_id(actualFlight.getId());
        anotherUserTicketDto.setSeat(15);
        anotherUserTicketDto.setService_class_id(3);
        anotherUserTicketDto.setPassenger_id(anotherPassenger.getId());
        anotherUserTicketDto.setBaggage_allowance("10 kg");

        ticketRepository=new TicketRepository(dataSource, new TicketMapper());
    }

    @AfterEach
    void destructor(){
        String sql="""
        drop table airport, booking_user, favorite_airports,
        flight, flyway_schema_history, passenger, role, service_class, ticket
        """;
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dataSource.close();
    }

    @Test
    void book_success() throws SQLException{
        //given
        String sql="select baggage_allowance from ticket where flight_id=? and seat=?";
        String actual;
        //when
        ticketRepository.book(ticketDto, dataSource.getConnection());
        try(Connection connection= dataSource.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setLong(1, ticketDto.getFlight_id());
            statement.setInt(2, ticketDto.getSeat());
            var rs=statement.executeQuery();
            rs.next();
            actual=rs.getString("baggage_allowance");
        }
        //then
        Assertions.assertEquals(ticketDto.getBaggage_allowance(), actual);
    }

    @Test
    void read_success() throws SQLException, MapperException{
        //given
        ticketRepository.book(ticketDto, dataSource.getConnection());
        //when
        var actual=ticketRepository.read(ticketDto.getFlight_id(), ticketDto.getSeat(), dataSource.getConnection());
        //then
        Assertions.assertEquals(passenger.getSurname(), actual.getSurname());
    }

    @Test
    void readActualByUser_success() throws SQLException, MapperException{
        //given
        ticketRepository.book(ticketDto, dataSource.getConnection());
        ticketRepository.book(actualTicketDto, dataSource.getConnection());
        ticketRepository.book(anotherUserTicketDto, dataSource.getConnection());
        //when
        var actual=ticketRepository.readActualByUser(user.getId());
        //then
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(actualFlight.getId(), actual.getFirst().getFlight_id());
    }

    @Test
    void readAllByUser_success() throws SQLException, MapperException {
        //given
        ticketRepository.book(ticketDto, dataSource.getConnection());
        ticketRepository.book(actualTicketDto, dataSource.getConnection());
        ticketRepository.book(anotherUserTicketDto, dataSource.getConnection());
        //when
        var actual=ticketRepository.readAllByUser(user.getId());
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(ticketDto.getFlight_id(), actual.getFirst().getFlight_id());
    }

    @Test
    void readAll_success() throws SQLException, MapperException {
        //given
        ticketRepository.book(ticketDto, dataSource.getConnection());
        ticketRepository.book(actualTicketDto, dataSource.getConnection());
        ticketRepository.book(anotherUserTicketDto, dataSource.getConnection());
        //when
        var actual=ticketRepository.readAll();
        Assertions.assertEquals(3, actual.size());
        Assertions.assertEquals(anotherUserTicketDto.getFlight_id(), actual.getLast().getFlight_id());
    }

    @Test
    void delete_success() throws SQLException, MapperException {
        //given
        ticketRepository.book(ticketDto, dataSource.getConnection());
        ticketRepository.book(actualTicketDto, dataSource.getConnection());
        ticketRepository.book(anotherUserTicketDto, dataSource.getConnection());
        //when
        ticketRepository.delete(anotherUserTicketDto.getFlight_id(), anotherUserTicketDto.getSeat(), dataSource.getConnection());
        var actual=ticketRepository.readAll();
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(actualTicketDto.getFlight_id(), actual.getLast().getFlight_id());
    }

    @Test
    void readUserIdByTicket_success() throws SQLException {
        //given
        ticketRepository.book(ticketDto, dataSource.getConnection());
        ticketRepository.book(actualTicketDto, dataSource.getConnection());
        ticketRepository.book(anotherUserTicketDto, dataSource.getConnection());
        //when
        var actual=ticketRepository.readOwnerByTicket(ticketDto.getFlight_id(), ticketDto.getSeat());
        //then
        Assertions.assertEquals(user.getId(), actual.get("user_id"));
        Assertions.assertEquals(ticketDto.getPassenger_id(), actual.get("passenger_id"));
    }



}
