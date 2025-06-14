package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class HelloMessage {
    
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    @Pattern(regexp = "[a-zA-Z0-9\\s]+", message = "Name can only contain alphanumeric characters and spaces")
    private String name;
    
    @NotBlank(message = "Message cannot be blank")
    @Size(min = 1, max = 200, message = "Message must be between 1 and 200 characters")
    private String message;
    
    public HelloMessage() {}
    
    public HelloMessage(String name, String message) {
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

    @Override
    public String toString() {
        return "HelloMessage{" + "name='" + name + '\'' + ", message='" + message + '\'' + '}';
    }
}
