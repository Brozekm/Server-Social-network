package com.brozek.socialnetwork.validation.exception;

public class StringResponse extends Throwable {
    private String response;

    public StringResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
