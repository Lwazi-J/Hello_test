package com.example.demo.helloapitest.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.demo.HelloApiApplication; // Make sure this points to your main application class

@CucumberContextConfiguration
@SpringBootTest(
        classes = HelloApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class CucumberSpringConfiguration {
    // This class can be empty - it's just used to configure the Cucumber-Spring integration
}