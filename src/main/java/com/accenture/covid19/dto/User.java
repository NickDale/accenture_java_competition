package com.accenture.covid19.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class User {

    @JsonProperty("user_id")
    private String userId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public User() {
    }

    public User(String userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
