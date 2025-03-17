package com.example.demo;

import java.util.Objects;



public class Greeting {
    private String id;
    private String message;
    private String name;

    // Default constructor for deserialization
    public Greeting() {
    }

    // Two-parameter constructor
    public Greeting(String id, String message) {
        this.id = id;
        this.message = message;
    }

    // Three-parameter constructor
    public Greeting(String id, String message, String name) {
        this.id = id;
        this.message = message;
        this.name = name;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // toString method (update to include name)
    @Override
    public String toString() {
        return "Greeting{" + "id='" + id + '\'' + ", message='" + message + '\'' + ", name='" + name + '\'' + '}';
    }

    // equals and hashCode (update to include name)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Greeting greeting = (Greeting) o;
        return Objects.equals(id, greeting.id) && Objects.equals(message, greeting.message) && Objects.equals(name, greeting.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, name);
    }
}