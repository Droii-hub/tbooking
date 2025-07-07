package com.walking.tbooking.repository;

import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.AirportMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.core.util.Assert;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AirportRepositoryTest {
    private HikariDataSource dataSource;
    private AirportRepository airportRepository;
    private AirportDto airportDto;

    @BeforeEach
    void setUp()throws SQLException{
        HikariConfig config=new HikariConfig("hikari_test.properties");
        dataSource=new HikariDataSource(config);
        FluentConfiguration flywayConfig= Flyway.configure()
                .locations("filesystem:" + System.getProperty("user.dir") +"/src/main/resources/db/migration")
                .dataSource(dataSource);
        Flyway flyway=flywayConfig.load();
        flyway.migrate();
        airportDto=new AirportDto();
        airportDto.setIata("OMS");
        airportDto.setName("Tsentralny");
        airportDto.setLocation("Omsk");
        airportRepository=new AirportRepository(dataSource, new AirportMapper());
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
        var actual=airportRepository.create(airportDto);
        //then
        Assertions.assertEquals(airportDto.getIata(), actual.getIata());
    }

    @Test
    void readByName_success() throws SQLException, MapperException{
        //given
        var firstAirport=airportRepository.create(airportDto);
        airportDto.setIata("OME");
        airportDto.setName("Nome");
        airportDto.setLocation("Alaska");
        var secondAirport=airportRepository.create(airportDto);
        //when
        var actual=airportRepository.readByName("Nome");
        //then
        Assertions.assertEquals(secondAirport.getName(), actual.getFirst().getName());
    }

    @Test
    void readByLocation_success() throws SQLException, MapperException{
        //given
        var firstAirport=airportRepository.create(airportDto);
        airportDto.setIata("OME");
        airportDto.setName("Nome");
        airportDto.setLocation("Alaska");
        var secondAirport=airportRepository.create(airportDto);
        //when
        var actual=airportRepository.readByLocation("Alaska");
        //then
        Assertions.assertEquals(secondAirport.getName(), actual.getFirst().getName());
    }

    @Test
    void readByIata_success() throws SQLException, MapperException{
        //given
        var airport=airportRepository.create(airportDto);
        //when
        var actual=airportRepository.readByIata("OMS");
        //then
        Assertions.assertEquals(airport.getName(), actual.getName());
    }

    @Test
    void update_success() throws SQLException, MapperException{
        //given
        var airport=airportRepository.create(airportDto);
        airportDto.setName("Central");
        //when
        var actual=airportRepository.update(airportDto);
        //then
        Assertions.assertEquals(airportDto.getName(), actual.getName());
    }

    @Test
    void delete_success() throws SQLException, MapperException{
        //given
        var firstAirport=airportRepository.create(airportDto);
        airportDto.setIata("OME");
        airportDto.setName("Nome");
        airportDto.setLocation("Alaska");
        var secondAirport=airportRepository.create(airportDto);
        //when
        airportRepository.delete("OMS");
        var airportList=airportRepository.readByName(null);
        //then
        Assertions.assertEquals(airportDto.getName(), airportList.getFirst().getName());
    }
}
