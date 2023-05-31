package ru.practicum;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = ru.practicum.statistics_client.client.StatClient.class)
public class StatisticServer {
    public static void main(String[] args) {
        SpringApplication.run(StatisticServer.class, args);
    }
}
