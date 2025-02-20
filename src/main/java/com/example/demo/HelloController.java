package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class HelloController {

    private List<Greeting> greetings = new ArrayList<>();

    private Optional<Greeting> findGreetingById(String id) {
        return greetings.stream().filter(g -> g.getId().equals(id)).findFirst();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/hello/{name}")
    public String helloWithName(@PathVariable String name) {
        return String.format("Hello %s", name);
    }

    @GetMapping("/hello/all")
    public List<Greeting> getAllGreetings()
    {
        return new ArrayList<>(greetings);
    }

    @PostMapping("/hello")
    public Greeting createGreeting(@RequestBody GreetingRequest request) {
        Greeting greeting = new Greeting(String.valueOf(greetings.size() + 1), String.format("Hello %s! Your message: %s", request.getName(), request.getMessage()));
        greetings.add(greeting);
        return greeting;
    }

    @PutMapping("/hello/{id}")
    public ResponseEntity<Greeting> updateGreeting(@PathVariable String id, @RequestBody GreetingRequest request) {
        Optional<Greeting> greetingOpt = findGreetingById(id);
        if (greetingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        int index = greetings.indexOf(greetingOpt.get());
        Greeting updatedGreeting = new Greeting(id, String.format("Hello %s! Your updated message: %s", request.getName(), request.getMessage()));
        greetings.set(index, updatedGreeting);
        return ResponseEntity.ok(updatedGreeting);
    }

    @DeleteMapping("/hello/{id}")
    public ResponseEntity<Void> deleteGreeting(@PathVariable String id) {Optional<Greeting> greetingOpt = findGreetingById(id);
        if (greetingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        greetings.remove(greetingOpt.get());
        return ResponseEntity.noContent().build();
    }

}
