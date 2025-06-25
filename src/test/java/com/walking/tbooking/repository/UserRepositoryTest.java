package com.walking.tbooking.repository;

import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.dto.user.UpdateUserDto;
import com.walking.tbooking.exception.BadRequestException;
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
import java.util.ArrayList;
import java.util.HashMap;

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
    public void createWithSameEmail_fail(){
        //given
        userRepository.create(createUserDto, 1);
        createUserDto.setSurname("Ivanov");
        //when
        BadRequestException thrown=Assertions.assertThrows(BadRequestException.class, ()->{
            userRepository.create(createUserDto, 1);
        });
        //then
        Assertions.assertTrue(thrown.getMessage()
                .contains("This email already in use"));
    }

    @Test
    public void createWithWrongEmail_fail(){
        //given
        createUserDto.setEmail("123");
        //when
        BadRequestException thrown=Assertions.assertThrows(BadRequestException.class, ()->{
            userRepository.create(createUserDto, 1);
        });
        //then
        Assertions.assertTrue(thrown.getMessage()
                .contains("Invalid email format"));
    }

    @Test
    public void readByEmail_success(){
        //given
        var user=userRepository.create(createUserDto, 1);
        //when
        var actual=userRepository.readByEmail(user.getEmail());
        //then
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    public void updateLastEnter_success(){
        //given
        var firstRequest=userRepository.create(createUserDto, 1);
        LocalDateTime before=LocalDateTime.now();

        //when
        userRepository.updateLastEnter(firstRequest.getId());
        LocalDateTime after=LocalDateTime.now();
        var secondRequest=userRepository.readByEmail(firstRequest.getEmail());
        //then
        Assertions.assertTrue(before.isBefore(secondRequest.getLastEnter())
                &secondRequest.getLastEnter().isBefore(after));
    }

    @Test
    void updateData_success(){
        //given
        var createdUser=userRepository.create(createUserDto, 1);
        UpdateUserDto changes=new UpdateUserDto();
        changes.setSurname("Ivanov");
        changes.setName(createdUser.getName());
        changes.setPatronymic(createdUser.getPatronymic());
        //when
        var updatedUser=userRepository.updateData(createdUser.getId(), changes);
        //then
        Assertions.assertEquals(changes.getSurname(), updatedUser.getSurname());
    }

    @Test
    void updatePassword() throws SQLException, MapperException {
        //given
        var createdUser=userRepository.create(createUserDto, 1);
        String beforeUpdate=userRepository.passwordById(createdUser.getId());
        //when
        userRepository.updatePassword(createdUser.getId(), "WeakPassword");
        String afterUpdate=userRepository.passwordById(createdUser.getId());
        //then
        Assertions.assertNotEquals(beforeUpdate, afterUpdate);
    }

    @Test
    void readAll_success() throws MapperException, SQLException{
        //given
        var createdUser=userRepository.create(createUserDto, 1);
        CreateUserDto secondUserDto=createUserDto;
        secondUserDto.setSurname("Ivanov");
        secondUserDto.setEmail("TestEmail@domain.al");
        var createdUser2=userRepository.create(secondUserDto, 1);
        //when
        ArrayList<ReadUserDto> list=new ArrayList<>(3);
        var temp=userRepository.readAll();
        list.addAll(temp);
        //then
        Assertions.assertEquals(createdUser.getSurname(), list.get(1).getSurname());
        Assertions.assertEquals(createdUser2.getSurname(), list.getLast().getSurname());
    }

    @Test
    void ban_success() throws SQLException, MapperException {
        //given
        var createdUser=userRepository.create(createUserDto, 1);
        //when
        userRepository.ban(createdUser.getId(), true);
        var actual=userRepository.readByEmail(createdUser.getEmail());
        //then
        Assertions.assertTrue(actual.isBlocked());
    }

    @Test
    void banMany_success() throws SQLException, MapperException{
        //given
        var firstUser=userRepository.create(createUserDto, 1);
        CreateUserDto secondUserDto=createUserDto;
        secondUserDto.setSurname("Ivanov");
        secondUserDto.setEmail("TestEmail@domain.al");
        var secondUser=userRepository.create(secondUserDto, 1);
        HashMap<Long, Boolean> banMap=new HashMap<>(3);
        banMap.put(firstUser.getId(), true);
        banMap.put(secondUser.getId(), true);
        //when
        userRepository.ban(banMap);
        var userList=userRepository.readAll();
        //then
        Assertions.assertTrue(userList.get(1).isBlocked());
        Assertions.assertTrue(userList.getLast().isBlocked());
    }
}
