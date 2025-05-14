package com.walking.tbooking.repository;

import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.SearchPassengerDto;
import com.walking.tbooking.dto.passenger.UpdatePassengerDto;
import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.exception.MapperException;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class PassengerRepositoryTest {
    private HikariDataSource dataSource;
    private PassengerRepository passengerRepository;
    private ReadUserDto firstUser;
    private CreatePassengerDto passengerDto;

    @BeforeEach
    void setUp() throws SQLException, MapperException {
        HikariConfig config=new HikariConfig("hikari_test.properties");
        dataSource=new HikariDataSource(config);
        FluentConfiguration flywayConfig= Flyway.configure()
                .locations("filesystem:" + System.getProperty("user.dir") +"/src/main/resources/db.migration")
                .dataSource(dataSource);
        Flyway flyway=flywayConfig.load();
        flyway.migrate();
        UserRepository userRepository = new UserRepository(dataSource, new UserMapper());
        CreateUserDto userDto=new CreateUserDto();
        userDto.setEmail("test@email.com");
        userDto.setSurname("Petrov");
        userDto.setName("Ivan");
        userDto.setPatronymic("Aleksandrovich");
        userDto.setPassword("GreatPassword");
        firstUser= userRepository.create(userDto, 2);
        userDto.setEmail("test2@email.com");
        userDto.setSurname("Ivanov");
        userDto.setName("Petr");
        ReadUserDto secondUser = userRepository.create(userDto, 2);
        passengerDto=new CreatePassengerDto();
        passengerDto.setSurname("Petrov");
        passengerDto.setName("Ivan");
        passengerDto.setPatronymic("Aleksandrovich");
        passengerDto.setMale(true);
        passengerDto.setBirth_date(LocalDate.of(1986,4,15));
        passengerDto.setPassport_series(9438);
        passengerDto.setPassport_number(876543);
        passengerDto.setPassport_source("UFMS g Arzamas");
        passengerDto.setPassport_issue_date(LocalDate.of(2002,4,27));

        passengerRepository=new PassengerRepository(dataSource,new PassengerMapper());
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
        var actual=passengerRepository.create(passengerDto, firstUser.getId());
        //then
        Assertions.assertEquals(actual.getSurname(), passengerDto.getSurname());
    }

    @Test
    void readByUserId_success() throws SQLException, MapperException{
        //given
        var firstPassenger=passengerRepository.create(passengerDto, firstUser.getId());
        passengerDto.setName("Sergey");
        var secondPassenger=passengerRepository.create(passengerDto, firstUser.getId());
        //when
        var passengerList=passengerRepository.readByUserId(firstUser.getId());
        //then
        Assertions.assertEquals("Ivan", passengerList.getFirst().getName());
        Assertions.assertEquals("Sergey", passengerList.getLast().getName());
    }

    @Test
    void readBySNP_success()throws SQLException, MapperException{
        //given
        var firstPassenger=passengerRepository.create(passengerDto, firstUser.getId());
        passengerDto.setName("Sergey");
        var secondPassenger=passengerRepository.create(passengerDto, firstUser.getId());
        SearchPassengerDto searchPassengerDto=new SearchPassengerDto();
        searchPassengerDto.setSurname("Petrov");
        //when
        var passengerList=passengerRepository.readBySNP(searchPassengerDto);
        //then
        Assertions.assertEquals(firstPassenger.getId(), passengerList.getFirst().getId());
        Assertions.assertEquals(secondPassenger.getId(), passengerList.getLast().getId());
    }

    @Test
    void readBySNP_success_name()throws SQLException, MapperException{
        //given
        var firstPassenger=passengerRepository.create(passengerDto, firstUser.getId());
        passengerDto.setName("Sergey");
        var secondPassenger=passengerRepository.create(passengerDto, firstUser.getId());
        SearchPassengerDto searchPassengerDto=new SearchPassengerDto();
        searchPassengerDto.setName("Ivan");
        //when
        var passengerList=passengerRepository.readBySNP(searchPassengerDto);
        //then
        Assertions.assertEquals(firstPassenger.getId(), passengerList.getLast().getId());
        Assertions.assertEquals(1, passengerList.size());
    }

    @Test
    void update_success()throws SQLException, MapperException{
        //given
        var firstPassenger=passengerRepository.create(passengerDto, firstUser.getId());
        UpdatePassengerDto updatePassengerDto=new UpdatePassengerDto();
        updatePassengerDto.setId(firstPassenger.getId());
        updatePassengerDto.setSurname(firstPassenger.getSurname());
        updatePassengerDto.setName("Sergey");
        updatePassengerDto.setPatronymic(firstPassenger.getPatronymic());
        updatePassengerDto.setMale(firstPassenger.isMale());
        updatePassengerDto.setBirth_date(firstPassenger.getBirth_date());
        updatePassengerDto.setPassport_series(firstPassenger.getPassport_series());
        updatePassengerDto.setPassport_number(firstPassenger.getPassport_number());
        updatePassengerDto.setPassport_source(firstPassenger.getPassport_source());
        updatePassengerDto.setPassport_issue_date(firstPassenger.getPassport_issue_date());
        //when
        var actual=passengerRepository.update(updatePassengerDto, firstUser.getId());
        //then
        Assertions.assertEquals("Sergey", actual.getName());
    }

    @Test
    void delete_success()throws SQLException,MapperException{
        //given
        var firstPassenger=passengerRepository.create(passengerDto, firstUser.getId());
        passengerDto.setName("Sergey");
        var secondPassenger=passengerRepository.create(passengerDto, firstUser.getId());
        //when
        passengerRepository.delete(firstPassenger.getId(),firstUser.getId());
        var passengerList=passengerRepository.readByUserId(firstUser.getId());
        //then
        Assertions.assertEquals(1, passengerList.size());
    }
}
