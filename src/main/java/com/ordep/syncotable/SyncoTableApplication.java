package com.ordep.syncotable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SyncoTableApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncoTableApplication.class, args);
    }

}
