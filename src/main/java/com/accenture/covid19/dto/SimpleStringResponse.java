package com.accenture.covid19.dto;

public class SimpleStringResponse {

    private String response;

    public SimpleStringResponse() {
    }

    public SimpleStringResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

