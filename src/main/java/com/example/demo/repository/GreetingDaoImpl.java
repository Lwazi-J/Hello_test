package com.example.demo.repository;

import com.example.demo.Greeting;
import com.example.demo.GreetingDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class GreetingDaoImpl implements GreetingDao {

    // Thread-safe collection for storing greetings
    private final ConcurrentMap<String, Greeting> greetingsMap = new ConcurrentHashMap<>();

    @Override
    public List<Greeting> findAll() {
        return new ArrayList<>(greetingsMap.values());
    }

    @Override
    public Optional<Greeting> findById(String id) {
        return Optional.ofNullable(greetingsMap.get(id));
    }

    @Override
    public Greeting save(Greeting greeting) {
        greetingsMap.put(greeting.getId(), greeting);
        return greeting;
    }

    @Override
    public Optional<Greeting> update(String id, Greeting greeting) {
        if (greetingsMap.containsKey(id)) {
            greeting.setId(id);
            greetingsMap.put(id, greeting);
            return Optional.of(greeting);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(String id) {
        return greetingsMap.remove(id) != null;
    }

    @Override
    public boolean exists(String id) {
        return greetingsMap.containsKey(id);
    }
}