package com.walking.tbooking.repository;

import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadByAirportsFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.AirportMapper;
import com.walking.tbooking.mapper.FlightMapper;
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
import java.time.LocalDateTime;

public class FlightRepositoryTest {
    private HikariDataSource dataSource;
    private FlightRepository flightRepository;
    private CreateFlightDto createFlightDto;

    @BeforeEach
    void setUp() throws SQLException, MapperException {
        HikariConfig config = new HikariConfig("hikari_test.properties");
        dataSource = new HikariDataSource(config);
        FluentConfiguration flywayConfig = Flyway.configure()
                .locations("filesystem:" + System.getProperty("user.dir") + "/src/main/resources/db.migration")
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

        flightRepository=new FlightRepository(dataSource, new FlightMapper());
        createFlightDto=new CreateFlightDto();
        createFlightDto.setDeparture_airport("OMS");
        createFlightDto.setDeparture_time(LocalDateTime.of(2025,5,13, 18,0,0));
        createFlightDto.setArrival_airport("OME");
        createFlightDto.setArrival_time(LocalDateTime.of(2025,5,13,23,0,0));
        createFlightDto.setTotal_seats(120);
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
    void create_success()throws SQLException, MapperException{
        //when
        var actual=flightRepository.create(createFlightDto);
        //then
        Assertions.assertEquals(createFlightDto.getArrival_airport(), actual.getArrival_airport());
    }

    @Test
    void readSeats_success() throws SQLException, MapperException{
        //given
        var flight=flightRepository.create(createFlightDto);
        //when
        var actual=flightRepository.readSeats(flight.getId());
        //then
        Assertions.assertEquals(createFlightDto.getTotal_seats(), actual.getTotalSeats());
    }

    @Test
    void readByAirports_success() throws SQLException, MapperException{
        //given
        var flight=flightRepository.create(createFlightDto);
        ReadByAirportsFlightDto readByAirportsFlightDto=new ReadByAirportsFlightDto();
        readByAirportsFlightDto.setDeparture_airport("OMS");
        //when
        var flightList=flightRepository.readByAirports(readByAirportsFlightDto);
        //then
        Assertions.assertEquals(flight.getDeparture_airport(),flightList.getFirst().getDeparture_airport());
    }

    @Test
    void readAll_success()throws SQLException, MapperException{
        //given
        var firstFlight=flightRepository.create(createFlightDto);
        createFlightDto.setDeparture_airport("OME");
        createFlightDto.setArrival_airport("OMS");
        var secondFlight=flightRepository.create(createFlightDto);
        //when
        var flightList=flightRepository.readAll();
        //then
        Assertions.assertEquals(firstFlight.getId(), flightList.getFirst().getId());
        Assertions.assertEquals(secondFlight.getId(), flightList.getLast().getId());
    }

    @Test
    void update_success() throws SQLException, MapperException{
        //given
        var flight=flightRepository.create(createFlightDto);
        flight.setAvailable_seats(90);
        flight.setTotal_seats(90);
        //when
        var actual=flightRepository.update(flight);
        //then
        Assertions.assertEquals(flight.getTotal_seats(), actual.getTotal_seats());
    }

    @Test
    void readById_success() throws SQLException, MapperException{
        //given
        var flight=flightRepository.create(createFlightDto);
        //when
        var actual=flightRepository.readById(flight.getId());
        //then
        Assertions.assertEquals(flight.getId(), actual.getId());
    }

    @Test
    void updateAvailableSeats_success() throws SQLException, MapperException{
        //given
        var flight=flightRepository.create(createFlightDto);
        //when
        flightRepository.updateAvailableSeats(flight.getId(),119);
        //then
        Assertions.assertEquals(119,
                flightRepository.readById(flight.getId()).getAvailable_seats());
    }

    @Test
    void delete_success()throws SQLException, MapperException{
        //given
        var firstFlight=flightRepository.create(createFlightDto);
        createFlightDto.setDeparture_airport("OME");
        createFlightDto.setArrival_airport("OMS");
        var secondFlight=flightRepository.create(createFlightDto);
        //when
        flightRepository.delete(firstFlight.getId());
        var flightList=flightRepository.readAll();
        //then
        Assertions.assertEquals(secondFlight.getId(), flightList.getFirst().getId());
    }
}
