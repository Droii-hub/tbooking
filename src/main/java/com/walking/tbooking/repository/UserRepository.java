package com.walking.tbooking.repository;

import com.walking.tbooking.PasswordProvider;
import com.walking.tbooking.dto.user.CreateUserDto;
import com.walking.tbooking.dto.user.ReadUserDto;
import com.walking.tbooking.dto.user.UpdateUserDto;
import com.walking.tbooking.exception.BadRequestException;
import com.walking.tbooking.mapper.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private final DataSource dataSource;
    private final Logger log= LogManager.getLogger(UserRepository.class);
    private final UserMapper userMapper;

    public UserRepository(DataSource dataSource, UserMapper userMapper){
        this.dataSource=dataSource;
        this.userMapper=userMapper;
    }

    public ReadUserDto create(CreateUserDto createUserDto, int roleId){
        String sql= """
                insert into booking_user (email, password, surname, name, patronymic, role_id)
                 values (?, ?, ?, ?, ?, ?) returning *
                """;
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, createUserDto.getEmail());
            statement.setString(2, PasswordProvider.hashPassword(createUserDto.getPassword()));
            statement.setString(3, createUserDto.getSurname());
            statement.setString(4, createUserDto.getName());
            statement.setString(5, createUserDto.getPatronymic());
            statement.setInt(6,roleId);
            var rs=statement.executeQuery();
            return userMapper.map(rs);
        } catch (SQLException e){
            if (e.getMessage().contains("ERROR: duplicate key value violates unique constraint \"booking_user_email_key\"")){
                log.info("This email already in use: {}", createUserDto.getEmail());
                throw new BadRequestException("This email already in use");
            }
            if (e.getMessage().contains("ERROR: new row for relation \"booking_user\" violates check constraint \"booking_user_email_check\"")){
                log.info("Invalid email format: {}", createUserDto.getEmail());
                throw new BadRequestException("Invalid email format");
            }
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateLastEnter(long id){
        String sql= "update booking_user set last_enter=? where id=?";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ReadUserDto updateData(UpdateUserDto updateUserDto){
        String sql= """
                update booking_user set surname=?,
                name=?,
                patronymic=? where id=? returning *""";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, updateUserDto.getSurname());
            statement.setString(2, updateUserDto.getName());
            statement.setString(3, updateUserDto.getPatronymic());
            statement.setLong(4, updateUserDto.getId());
            var rs=statement.executeQuery();
            return userMapper.map(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updatePassword(long id, String password){
        String sql= """
                update booking_user set password=? where id=?""";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, PasswordProvider.hashPassword(password));
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ReadUserDto readByEmail(String email){
        String sql= """
                select id, email, surname, name, patronymic, last_enter, blocked, role_id
                from booking_user
                where email=?
                """;
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, email);
            var rs=statement.executeQuery();
            return userMapper.map(rs);
        } catch (SQLException e) {
            if (e.getMessage().contains("ResultSet have not value")) {
                log.info("Wrong email: {}", email);
                throw new BadRequestException("Wrong email");
            }
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String passwordById(long id){
        String sql= """
                select password from booking_user where id=?""";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, id);
            var rs=statement.executeQuery();
            if(!rs.next())
                return  null;
            return rs.getString("password");
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<ReadUserDto> readAll(){
        String sql = """
                select id, email, surname, name, patronymic, last_enter, blocked, role_id
                from booking_user
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            var rs = statement.executeQuery();
            return userMapper.mapMany(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void ban(long id, boolean ban){
        String sql="update booking_user set blocked=? where id=?";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setBoolean(1, ban);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void ban(Map<Long,Boolean> banList){
        String sql="update booking_user set blocked=? where id=?";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            for(Map.Entry<Long,Boolean> entry:banList.entrySet()){
                statement.setBoolean(1, entry.getValue());
                statement.setLong(2, entry.getKey());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}