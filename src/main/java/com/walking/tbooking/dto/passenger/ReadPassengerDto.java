package com.walking.tbooking.dto.passenger;

import java.time.LocalDate;

public class ReadPassengerDto {
    private long id;
    private long user_id;
    private String surname;
    private String name;
    private String patronymic;
    private boolean male;
    private LocalDate birth_date;
    private int passport_series;
    private int passport_number;
    private String passport_source;
    private LocalDate passport_issue_date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public int getPassport_series() {
        return passport_series;
    }

    public void setPassport_series(int passport_series) {
        this.passport_series = passport_series;
    }

    public int getPassport_number() {
        return passport_number;
    }

    public void setPassport_number(int passport_number) {
        this.passport_number = passport_number;
    }

    public String getPassport_source() {
        return passport_source;
    }

    public void setPassport_source(String passport_source) {
        this.passport_source = passport_source;
    }

    public LocalDate getPassport_issue_date() {
        return passport_issue_date;
    }

    public void setPassport_issue_date(LocalDate passport_issue_date) {
        this.passport_issue_date = passport_issue_date;
    }
}
