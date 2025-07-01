package com.walking.tbooking.repository;

import com.walking.tbooking.dto.flight.CreateFlightDto;
import com.walking.tbooking.dto.flight.ReadByAirportsFlightDto;
import com.walking.tbooking.dto.flight.ReadFlightDto;
import com.walking.tbooking.dto.flight.SeatsDto;
import com.walking.tbooking.mapper.FlightMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class FlightRepository {
    private final DataSource dataSource;
    private final Logger log= LogManager.getLogger(FlightRepository.class);
    private final FlightMapper mapper;

    public FlightRepository(DataSource dataSource, FlightMapper mapper){
        this.dataSource=dataSource;
        this.mapper=mapper;
    }

    public ReadFlightDto create(CreateFlightDto flight){
        String sql= """
                insert into flight values(default, ?, ?, ?, ?, ?, ?)
                returning *
                """;
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, flight.getDeparture_airport());
            statement.setTimestamp(2, Timestamp.valueOf(flight.getDeparture_time()));
            statement.setString(3, flight.getArrival_airport());
            statement.setTimestamp(4, Timestamp.valueOf(flight.getArrival_time()));
            statement.setInt(5, flight.getTotal_seats());
            statement.setInt(6, flight.getTotal_seats());
            var rs=statement.executeQuery();
            return mapper.map(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public ReadFlightDto readById(long id){
        String sql="select * from flight where id=?";
        try(Connection connection=dataSource.getConnection();
        PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1,id);
            var rs=statement.executeQuery();
            return mapper.map(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public SeatsDto readSeats(long id){
        String sql="select seat from ticket where flight_id=? order by seat";
        String totalSql="select total_seats from flight where id=?";
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql);
            PreparedStatement statementTotal=connection.prepareStatement(totalSql)){
            statement.setLong(1, id);
            statementTotal.setLong(1, id);
            var rs=statement.executeQuery();
            var rsTotal=statementTotal.executeQuery();
            if (!rsTotal.next())
                throw new SQLException("Flight with this id does not exists");
            SeatsDto result=new SeatsDto();
            result.setTotalSeats(rsTotal.getInt("total_seats"));
            LinkedList<Integer> unavailableSeats=new LinkedList<>();
            while (rs.next())
                unavailableSeats.add(rs.getInt("seat"));
            result.setUnavailableSeats(unavailableSeats);
            return  result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public int readAvailableSeats(Long id, Connection connection){
        String sql="select available_seats from flight where id=?";
        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setLong(1,id);
            var rs=statement.executeQuery();
            if (!rs.next())
                throw new SQLException("Flight with this id does not exists");
            return rs.getInt("available_seats");
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new RuntimeException("Ошибка при чтении с базы "+e.getMessage());
        }
    }

    public List<ReadFlightDto> readByAirports(ReadByAirportsFlightDto airports){
        String sql="select * from flight where departure_airport ilike ? and arrival_airport ilike ?";
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, airports.getDeparture_airport()==null ? "%" : airports.getDeparture_airport());
            statement.setString(2, airports.getArrival_airport()==null ? "%" : airports.getArrival_airport());
            var rs=statement.executeQuery();
            return mapper.mapMany(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ReadFlightDto> readAll(){
        String sql="select * from flight";
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            var rs=statement.executeQuery();
            return mapper.mapMany(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public ReadFlightDto update(ReadFlightDto flight){
        String sql= """
                update flight set
                departure_airport=?,
                departure_time=?,
                arrival_airport=?,
                arrival_time=?,
                available_seats=?,
                total_seats=?
                where id=? returning *""";
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, flight.getDeparture_airport());
            statement.setTimestamp(2, Timestamp.valueOf(flight.getDeparture_time()));
            statement.setString(3, flight.getArrival_airport());
            statement.setTimestamp(4, Timestamp.valueOf(flight.getArrival_time()));
            statement.setInt(5, flight.getTotal_seats());
            statement.setInt(6, flight.getTotal_seats());
            statement.setLong(7, flight.getId());
            var rs=statement.executeQuery();
            return mapper.map(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateAvailableSeats(long id, int availableSeats, Connection connection){
        String sql="update flight set available_seats=? where id=?";
        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1,availableSeats);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Ошибка при записи в базу "+e.getMessage());
        }
    }

    public void delete(long id){
        String sql="delete from flight where id=?";
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
