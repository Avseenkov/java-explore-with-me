package ru.practicum.main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients(basePackageClasses = ru.practicum.statistics_client.client.StatClient.class)
public class MainApplication {


    public static void main(String[] args) {

        SpringApplication.run(MainApplication.class, args);

    }

    @Bean
    public CommandLineRunner runClient(Test test) {
        return args -> {
            //test.getStat();
        };
    }
}
