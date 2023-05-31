package com.example.springboot_securitydemo.util;

public class IncorrectUserDataException extends RuntimeException {

    private String messages;

    public IncorrectUserDataException(String messages) {
        this.messages = messages;
    }

    public String getMessages() {
        return messages;
    }
}
