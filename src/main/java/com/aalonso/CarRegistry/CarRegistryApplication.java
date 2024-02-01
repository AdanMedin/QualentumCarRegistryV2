package com.aalonso.CarRegistry;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CarRegistryApplication {

    @PostConstruct
    public void init() {
        log.info("**************************************");
        log.info("CAR REGISTRY APPLICATION RUNNING...");
        log.info("**************************************");
        log.info("CarRegistryApplication is operational...");
    }

    public static void main(String[] args) {
        SpringApplication.run(CarRegistryApplication.class, args);
    }

}
