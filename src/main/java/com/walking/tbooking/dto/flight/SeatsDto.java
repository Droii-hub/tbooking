package com.walking.tbooking.dto.flight;

import java.util.List;

public class SeatsDto {
    private List<Integer> unavailableSeats;
    private int totalSeats;

    public List<Integer> getUnavailableSeats() {
        return unavailableSeats;
    }

    public void setUnavailableSeats(List<Integer> unavailableSeats) {
        this.unavailableSeats = unavailableSeats;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}
