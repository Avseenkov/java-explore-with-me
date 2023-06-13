package ru.practicum.main.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ewm.service")
public class EwmServiceConfig {

    public String defaultDateFormat;

    public String nameApp;
}
