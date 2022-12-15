package ru.x5.demo.kafka.saga.ordersaga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class OrderSagaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderSagaApplication.class, args);
    }

}
