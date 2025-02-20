package com.example.demo;

import java.util.List;
import java.util.Optional;

public interface GreetingDao {
    List<Greeting> findAll();
    Optional<Greeting> findById(String id);
    Greeting save(Greeting greeting);
    Optional<Greeting> update(String id, Greeting greeting);
    boolean delete(String id);
    boolean exists(String id);
}
