package com.ardas.stockorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StockOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockOrderServiceApplication.class, args);
    }

}
