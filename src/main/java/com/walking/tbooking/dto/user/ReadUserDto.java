package com.walking.tbooking.dto.user;

import java.time.LocalDateTime;

public class ReadUserDto {
    private long id;
    private String email;
    private String surname;
    private String name;
    private String patronymic;
    private LocalDateTime lastEnter;
    private boolean isBlocked;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDateTime getLastEnter() {
        return lastEnter;
    }

    public void setLastEnter(LocalDateTime lastEnter) {
        this.lastEnter = lastEnter;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
