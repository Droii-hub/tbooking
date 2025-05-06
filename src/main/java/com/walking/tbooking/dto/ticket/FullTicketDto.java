package com.walking.tbooking.dto.ticket;

import java.time.LocalDateTime;

public class FullTicketDto {
    private long flight_id;
    private String departure_airport;
    private LocalDateTime departure_time;
    private String arrival_airport;
    private LocalDateTime arrival_time;
    private int seat;
    private String service_class;
    private String baggage_allowance;
    private String surname;
    private String name;
    private String patronymic;

    public long getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(long flight_id) {
        this.flight_id = flight_id;
    }

    public String getDeparture_airport() {
        return departure_airport;
    }

    public void setDeparture_airport(String departure_airport) {
        this.departure_airport = departure_airport;
    }

    public LocalDateTime getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(LocalDateTime departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrival_airport() {
        return arrival_airport;
    }

    public void setArrival_airport(String arrival_airport) {
        this.arrival_airport = arrival_airport;
    }

    public LocalDateTime getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(LocalDateTime arrival_time) {
        this.arrival_time = arrival_time;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getService_class() {
        return service_class;
    }

    public void setService_class(String service_class) {
        this.service_class = service_class;
    }

    public String getBaggage_allowance() {
        return baggage_allowance;
    }

    public void setBaggage_allowance(String baggage_allowance) {
        this.baggage_allowance = baggage_allowance;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
}
