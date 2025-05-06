package com.walking.tbooking.repository;

import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.UserMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    private HikariDataSource dataSource;
    private UserRepository userRepository;
    private CreateUserDto createUserDto;

    @BeforeEach
    public void setUp(){
        HikariConfig config=new HikariConfig("hikari_test.properties");
        dataSource=new HikariDataSource(config);
        FluentConfiguration flywayConfig= Flyway.configure()
                .locations("filesystem:" + System.getProperty("user.dir") +"/src/main/resources/db.migration")
                .dataSource(dataSource);
        Flyway flyway=flywayConfig.load();
        flyway.migrate();
        userRepository=new UserRepository(dataSource,new UserMapper());
        createUserDto=new CreateUserDto();
        createUserDto.setEmail("test@email.com");
        createUserDto.setSurname("Petrov");
        createUserDto.setName("Ivan");
        createUserDto.setPatronymic("Aleksandrovich");
        createUserDto.setPassword("GreatPassword");
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
    public void create_success(){
        //given
        ReadUserDto expected=new ReadUserDto();
        expected.setEmail(createUserDto.getEmail());
        //when
        try {
            var actual = userRepository.create(createUserDto, 1);
            //then
            Assertions.assertEquals(expected.getEmail(), actual.getEmail());
            Assertions.assertNull(actual.getLastEnter());
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    public void createWithSameEmail_fail() throws SQLException, MapperException {
        //given
        userRepository.create(createUserDto, 1);
        createUserDto.setSurname("Ivanov");
        //when
        SQLException thrown=Assertions.assertThrows(SQLException.class, ()->{
            userRepository.create(createUserDto, 1);
        });
        //then
        Assertions.assertTrue(thrown.getMessage()
                .contains("ERROR: duplicate key value violates unique constraint \"booking_user_email_key\""));
    }

    @Test
    public void readById_success() throws SQLException, MapperException {
        //given
        var user=userRepository.create(createUserDto, 1);
        //when
        var actual=userRepository.readById(user.getId());
        //then
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    public void updateLastEnter_success() throws SQLException, MapperException {
        //given
        var firstRequest=userRepository.create(createUserDto, 1);
        LocalDateTime before=LocalDateTime.now();

        //when
        userRepository.updateLastEnter(firstRequest.getId());
        LocalDateTime after=LocalDateTime.now();
        var secondRequest=userRepository.readById(firstRequest.getId());
        //then
        Assertions.assertTrue(before.isBefore(secondRequest.getLastEnter())
                &secondRequest.getLastEnter().isBefore(after));
    }
}
