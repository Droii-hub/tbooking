package com.walking.tbooking.dto.ticket;

public class TicketDto {
    private long flight_id;
    private int seat;
    private int service_class_id;
    private String baggage_allowance;
    private long passenger_id;

    public long getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(long flight_id) {
        this.flight_id = flight_id;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getService_class_id() {
        return service_class_id;
    }

    public void setService_class_id(int service_class_id) {
        this.service_class_id = service_class_id;
    }

    public String getBaggage_allowance() {
        return baggage_allowance;
    }

    public void setBaggage_allowance(String baggage_allowance) {
        this.baggage_allowance = baggage_allowance;
    }

    public long getPassenger_id() {
        return passenger_id;
    }

    public void setPassenger_id(long passenger_id) {
        this.passenger_id = passenger_id;
    }
}
