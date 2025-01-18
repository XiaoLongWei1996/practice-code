package com.xlw.spring_design_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.config.EnableStateMachine;

@EnableStateMachine
@SpringBootApplication
public class SpringDesignDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDesignDemoApplication.class, args);
    }

}
