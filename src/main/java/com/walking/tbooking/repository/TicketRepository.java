package com.walking.tbooking.repository;

import com.walking.tbooking.dto.ticket.FullTicketDto;
import com.walking.tbooking.dto.ticket.TicketDto;
import com.walking.tbooking.exception.MapperException;
import com.walking.tbooking.mapper.TicketMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class TicketRepository {
    private final Logger log= LogManager.getLogger(TicketRepository.class);
    private final DataSource dataSource;
    private final TicketMapper mapper;

    public TicketRepository(DataSource dataSource, TicketMapper mapper){
        this.dataSource=dataSource;
        this.mapper=mapper;
    }

    public List<FullTicketDto> readActualByUser(long user_id) throws MapperException, SQLException{
        String sql="""
                   select flight_id, departure_airport, departure_time, arrival_airport, arrival_time, seat,
                   class, baggage_allowance, surname, name, patronymic
                   from ticket t
                   join (select id, departure_airport, departure_time, arrival_airport, arrival_time
                   from flight where departure_time>?) f on t.flight_id=f.id
                   join service_class s on t.service_class_id=s.id
                   join (select id, surname, name, patronymic from passenger where user_id=?) p on t.passenger_id=p.id
                   """;
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(2, user_id);
            var rs=statement.executeQuery();
            return mapper.mapMany(rs);
        }
    }

    public List<FullTicketDto> readAllByUser(long user_id) throws MapperException, SQLException{
        String sql="""
                   select flight_id, departure_airport, departure_time, arrival_airport, arrival_time, seat,
                   class, baggage_allowance, surname, name, patronymic
                   from ticket t
                   join flight f on t.flight_id=f.id
                   join service_class s on t.service_class_id=s.id
                   join (select id, surname, name, patronymic from passenger where user_id=?) p on t.passenger_id=p.id
                   """;
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setLong(1, user_id);
            var rs=statement.executeQuery();
            return mapper.mapMany(rs);
        }
    }

    public List<FullTicketDto> readAll() throws MapperException, SQLException{
        String sql="""
                   select flight_id, departure_airport, departure_time, arrival_airport, arrival_time, seat,
                   class, baggage_allowance, surname, name, patronymic
                   from ticket t
                   join flight f on t.flight_id=f.id
                   join service_class s on t.service_class_id=s.id
                   join passenger p on t.passenger_id=p.id
                   """;
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            var rs=statement.executeQuery();
            return mapper.mapMany(rs);
        }
    }

    public void book(TicketDto ticket) throws SQLException{
        String sql="insert into ticket values (?, ?, ?, ?, ?)";
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setLong(1, ticket.getFlight_id());
            statement.setInt(2,ticket.getSeat());
            statement.setInt(3,ticket.getService_class_id());
            statement.setString(4,ticket.getBaggage_allowance());
            statement.setLong(5, ticket.getPassenger_id());
            statement.executeUpdate();
        }
    }

    public FullTicketDto read(long flight_id, int seat) throws MapperException, SQLException{
        String sql="""
                   select flight_id, departure_airport, departure_time, arrival_airport, arrival_time, seat,
                   class, baggage_allowance, surname, name, patronymic
                   from ticket t
                   join flight f on t.flight_id=f.id
                   join service_class s on t.service_class_id=s.id
                   join passenger p on t.passenger_id=p.id
                   where flight_id=? and seat=?
                   """;
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setLong(1,flight_id);
            statement.setInt(2, seat);
            var rs=statement.executeQuery();
            return mapper.map(rs);
        }
    }

    public void delete(long flight_id, int seat) throws MapperException, SQLException{
        String sql="delete from ticket where flight_id=? and seat=?";
        try(Connection connection= dataSource.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql)) {
            statement.setLong(1, flight_id);
            statement.setInt(2, seat);
            statement.executeUpdate();
        }
    }
}
