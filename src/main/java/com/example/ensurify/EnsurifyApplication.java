package com.example.ensurify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EnsurifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnsurifyApplication.class, args);
    }

}
