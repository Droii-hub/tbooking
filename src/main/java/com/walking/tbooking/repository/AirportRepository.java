package com.walking.tbooking.repository;

import com.walking.tbooking.dto.airport.AirportDto;
import com.walking.tbooking.mapper.AirportMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AirportRepository {
    private final Logger log= LogManager.getLogger(AirportRepository.class);
    private final DataSource dataSource;
    private final AirportMapper mapper;

    public AirportRepository(DataSource dataSource, AirportMapper mapper){
        this.dataSource=dataSource;
        this.mapper=mapper;
    }

    public AirportDto create(AirportDto airport){
        String sql= "insert into airport values (?, ?, ?) returning *";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, airport.getIata());
            statement.setString(2, airport.getName());
            statement.setString(3, airport.getLocation());
            var rs=statement.executeQuery();
            return mapper.map(rs);
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<AirportDto> readByName(String name){
        String sql="select * from airport where name ilike ?";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, name==null ? "%" : name);
            var rs=statement.executeQuery();
            return mapper.mapMany(rs);
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<AirportDto> readByLocation(String location){
        String sql="select * from airport where location ilike ?";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, location==null ? "%" : location);
            var rs=statement.executeQuery();
            return mapper.mapMany(rs);
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public AirportDto readByIata(String iata){
        String sql="select * from airport where iata=?";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, iata);
            var rs=statement.executeQuery();
            return mapper.map(rs);
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public AirportDto update(AirportDto airport){
        String sql= """
                update airport set
                name=?,
                location=?
                where iata=? returning *
                """;
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, airport.getName());
            statement.setString(2, airport.getLocation());
            statement.setString(3, airport.getIata());
            var rs=statement.executeQuery();
            return mapper.map(rs);
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(String iata){
        String sql="delete from airport where iata=?";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, iata);
            statement.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}























