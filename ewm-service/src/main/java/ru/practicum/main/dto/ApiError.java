package ru.practicum.main.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import ru.practicum.main.utils.Settings;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ApiError {

    private List<String> errors;

    private String message;

    private String reason;

    private HttpStatus status;

    private String timestamp;

    public ApiError(HttpStatus status, String message, List<String> errors, String reason, LocalDateTime date) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.reason = reason;
        this.timestamp = Settings.getFormatter().format(date);
    }

    public ApiError(HttpStatus status, String message, String error, String reason, LocalDateTime date) {
        this.status = status;
        this.message = message;
        this.errors = Arrays.asList(error);
        this.reason = reason;
        this.timestamp = Settings.getFormatter().format(date);
    }
}
