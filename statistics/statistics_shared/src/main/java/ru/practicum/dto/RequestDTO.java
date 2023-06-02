package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class RequestDTO {


    public Long id;

    @NotNull
    public String app;

    @NotNull
    public String ip;

    public String timestamp;

    @NotNull
    public String uri;
}
