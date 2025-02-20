package com.example.demo;

public class GreetingRequest {
    private String name;
    private String message;

    // Default constructor for JSON deserialization
    public GreetingRequest() {}

    public GreetingRequest(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}