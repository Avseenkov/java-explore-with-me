package ru.practicum.main.utils;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.main.config.EwmServiceConfig;

import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class Settings {

    private static EwmServiceConfig config;

    public static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(config.getDefaultDateFormat());
    }

    @Autowired
    private void setConfig(EwmServiceConfig ewmConfig) {
        config = ewmConfig;
    }

    public static String getNameApp() {
        return config.getNameApp();
    }
}
