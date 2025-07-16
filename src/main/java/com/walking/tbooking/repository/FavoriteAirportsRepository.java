package com.walking.tbooking.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class FavoriteAirportsRepository {
    private final Logger log= LogManager.getLogger(FavoriteAirportsRepository.class);
    private final DataSource dataSource;

    public FavoriteAirportsRepository(DataSource dataSource){
        this.dataSource=dataSource;
    }

    public void updateFavoriteAirports(long passengerId, ArrayList<String> airportList){
        String sql="""
        insert into favorite_airports (passenger_id, first_airport, second_airport, third_airport)
        values (?, ?, ?, ?)
        ON CONFLICT (passenger_id) DO UPDATE
        set first_airport=?, second_airport=?, third_airport=?
        """;
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setLong(1,passengerId);
            statement.setString(2, airportList.get(0));
            statement.setString(3, airportList.get(1));
            statement.setString(4, airportList.get(2));
            statement.setString(5, airportList.get(0));
            statement.setString(6, airportList.get(1));
            statement.setString(7, airportList.get(2));
            statement.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(long passengerId){
        String sql="delete from favorite_airports where passenger_id=?";
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)) {
            statement.setLong(1, passengerId);
            statement.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
