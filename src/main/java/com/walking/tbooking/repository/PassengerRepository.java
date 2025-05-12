package com.walking.tbooking.repository;

import com.walking.tbooking.dto.passenger.CreatePassengerDto;
import com.walking.tbooking.dto.passenger.ReadPassengerDto;
import com.walking.tbooking.dto.passenger.SearchPassengerDto;
import com.walking.tbooking.dto.passenger.UpdatePassengerDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.PassengerMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class PassengerRepository {
    private final DataSource dataSource;
    private final Logger log= LogManager.getLogger(PassengerRepository.class);
    private final PassengerMapper mapper;

    public PassengerRepository(DataSource dataSource, PassengerMapper mapper){
        this.dataSource=dataSource;
        this.mapper=mapper;
    }

    public ReadPassengerDto create(CreatePassengerDto createPassengerDto, long userId) throws MapperException, SQLException {
        String sql= """
                insert into passenger (user_id, surname, name, patronymic, male,
                 birth_date, passport_series, passport_number, passport_source, passport_issue_date)
                 values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                 returning *
                """;
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, userId);
            statement.setString(2, createPassengerDto.getSurname());
            statement.setString(3, createPassengerDto.getName());
            statement.setString(4, createPassengerDto.getPatronymic());
            statement.setBoolean(5, createPassengerDto.isMale());
            statement.setDate(6, Date.valueOf(createPassengerDto.getBirth_date()));
            statement.setInt(7, createPassengerDto.getPassport_series());
            statement.setInt(8, createPassengerDto.getPassport_number());
            statement.setString(9, createPassengerDto.getPassport_source());
            statement.setDate(10, Date.valueOf(createPassengerDto.getPassport_issue_date()));
            var rs=statement.executeQuery();
            return mapper.map(rs);
        }
    }

    public List<ReadPassengerDto> readByUserId(long user_id) throws MapperException, SQLException{
        String sql= """
                select * from passenger where user_id=?
                """;
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, user_id);
            var rs=statement.executeQuery();
            return mapper.mapMany(rs);
        }
    }

    public List<ReadPassengerDto> readBySNP(SearchPassengerDto passenger) throws MapperException, SQLException{
        String sql= """
                select * from passenger where
                surname ilike ? and
                name ilike ? and
                patronymic ilike ?
                """;
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, passenger.getSurname()==null ? "%" : passenger.getSurname());
            statement.setString(2, passenger.getName()==null ? "%" : passenger.getName());
            statement.setString(3, passenger.getPatronymic()==null ? "%" : passenger.getPatronymic());
            var rs=statement.executeQuery();
            return mapper.mapMany(rs);
        }
    }

    public ReadPassengerDto update(UpdatePassengerDto passenger, long user_id) throws MapperException, SQLException{
        String checkSql="select user_id from passenger where id=?";
        String updateSql= """
                update passenger set
                surname=?,
                name=?,
                patronymic=?,
                male=?,
                birth_date=?,
                passport_series=?,
                passport_number=?,
                passport_source=?,
                passport_issue_date=? where id=? returning *
                """;
        try(Connection connection=dataSource.getConnection();
        PreparedStatement checkStatement= connection.prepareStatement(checkSql);
        PreparedStatement updateStatement= connection.prepareStatement(updateSql)){
            checkStatement.setLong(1, passenger.getId());
            ResultSet rs=checkStatement.executeQuery();
            if (!rs.next()){
                throw new SQLException("Passenger with this id is not exist");
            } else{
                if(user_id!=rs.getLong("user_id"))
                    throw new SQLException("Passenger id does not match user id");
            }
            updateStatement.setString(1, passenger.getSurname());
            updateStatement.setString(2, passenger.getName());
            updateStatement.setString(3, passenger.getPatronymic());
            updateStatement.setBoolean(4, passenger.isMale());
            updateStatement.setDate(5, Date.valueOf(passenger.getBirth_date()));
            updateStatement.setInt(6, passenger.getPassport_series());
            updateStatement.setInt(7, passenger.getPassport_number());
            updateStatement.setString(8, passenger.getPassport_source());
            updateStatement.setDate(9, Date.valueOf(passenger.getPassport_issue_date()));
            updateStatement.setLong(10, passenger.getId());
            var resultSet=updateStatement.executeQuery();
            return mapper.map(resultSet);
        }
    }

    public void delete(long passenger_id, long user_id) throws SQLException{
        String checkSql="select user_id from passenger where id=?";
        String deleteSql= """
                delete from passenger where id=?
                """;
        try(Connection connection=dataSource.getConnection();
            PreparedStatement checkStatement= connection.prepareStatement(checkSql);
            PreparedStatement deleteStatement= connection.prepareStatement(deleteSql)) {
            checkStatement.setLong(1, passenger_id);
            ResultSet rs = checkStatement.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Passenger with this id is not exist");
            } else {
                if (user_id != rs.getLong("user_id"))
                    throw new SQLException("Passenger id does not match user id");
            }
            deleteStatement.setLong(1, passenger_id);
            deleteStatement.executeUpdate();
        }
    }
}
