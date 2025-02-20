package com.example.demo;

import java.util.List;
import java.util.Optional;

public interface GreetingService {
    List<Greeting> getAllGreetings();
    Optional<Greeting> getGreetingById(String id);
    Greeting createGreeting(GreetingRequest request);
    Optional<Greeting> updateGreeting(String id, GreetingRequest request);
    boolean deleteGreeting(String id);
}
