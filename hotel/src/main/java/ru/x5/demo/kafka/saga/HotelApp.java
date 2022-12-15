package ru.x5.demo.kafka.saga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
public class HotelApp {
    public static void main(String[] args) {
        SpringApplication.run(HotelApp.class, args);
    }
}
