package ru.practicum.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackageClasses = ru.practicum.statistics_client.client.StatClient.class)
public class MainApplication {


    public static void main(String[] args) {

        SpringApplication.run(MainApplication.class, args);

    }

}
