package com.personalfinancetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PersonalfinancetrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalfinancetrackerApplication.class, args);
    }

}
