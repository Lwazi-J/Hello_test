package com.example.demo.helloapitest.config;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "com.example.demo.helloapitest.config,com.example.demo.helloapitest.stepdefinitions")
@ConfigurationParameter(key = "cucumber.plugin", value = "json:target/cucumber-reports/cucumber.json")
public class CucumberTestRunner {
}
