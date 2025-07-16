package com.walking.tbooking.repository;

import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.mapper.AirportMapper;
import com.walking.tbooking.mapper.PassengerMapper;
import com.walking.tbooking.mapper.UserMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class FavoriteAirportsRepositoryTest {
    private HikariDataSource dataSource;
    private FavoriteAirportsRepository FArepository;
    private ReadPassengerDto passenger;

    @BeforeEach
    public void setUp() {
        HikariConfig config = new HikariConfig("hikari_test.properties");
        dataSource = new HikariDataSource(config);
        FluentConfiguration flywayConfig = Flyway.configure()
                .locations("filesystem:" + System.getProperty("user.dir") + "/src/main/resources/db/migration")
                .dataSource(dataSource);
        Flyway flyway = flywayConfig.load();
        flyway.migrate();
        FArepository =new FavoriteAirportsRepository(dataSource);
        UserRepository userRepository = new UserRepository(dataSource, new UserMapper());
        CreateUserDto userDto=new CreateUserDto();
        userDto.setEmail("test@email.com");
        userDto.setSurname("Petrov");
        userDto.setName("Ivan");
        userDto.setPatronymic("Aleksandrovich");
        userDto.setPassword("GreatPassword");
        ReadUserDto firstUser= userRepository.create(userDto, 2);
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

        PassengerRepository passengerRepository=new PassengerRepository(dataSource,new PassengerMapper());
        passenger=passengerRepository.create(passengerDto, firstUser.getId());
        AirportRepository airportRepository=new AirportRepository(dataSource, new AirportMapper());
        AirportDto airportDto=new AirportDto();
        airportDto.setIata("KJA");
        airportDto.setLocation("Krasn");
        airportDto.setName("Airport");

        airportRepository.create(airportDto);
        airportDto.setIata("IKT");
        airportRepository.create(airportDto);

    }

    @AfterEach
    public void destructor(){
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
    public void updateFavoriteAirports() throws SQLException {
        //given
        ArrayList<String> airportList=new ArrayList<>();
        airportList.add("KJA");
        airportList.add("IKT");
        airportList.add("IKT");
        //when
        FArepository.updateFavoriteAirports(passenger.getId(), airportList);
        String sql="select * from favorite_airports";
        try(Connection connection=dataSource.getConnection();
        PreparedStatement statement=connection.prepareStatement(sql)){
            var rs=statement.executeQuery();
            rs.next();
            //then
            Assertions.assertEquals("KJA", rs.getString("first_airport"));
            Assertions.assertEquals("IKT", rs.getString("second_airport"));
            Assertions.assertEquals("IKT", rs.getString("third_airport"));
        }
    }

    @Test
    public void delete_success() throws SQLException{
        //given
        ArrayList<String> airportList=new ArrayList<>();
        airportList.add("KJA");
        airportList.add("IKT");
        airportList.add("IKT");
        //when
        FArepository.updateFavoriteAirports(passenger.getId(), airportList);
        FArepository.delete(passenger.getId());
        String sql="select * from favorite_airports";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            var rs=statement.executeQuery();
            //then
            Assertions.assertFalse(rs.next());
        }
    }
}
