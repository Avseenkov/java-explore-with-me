package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class RequestDTO {


    private Long id;

    @NotNull
    private String app;

    @NotNull
    private String ip;

    private String timestamp;

    @NotNull
    private String uri;
}
