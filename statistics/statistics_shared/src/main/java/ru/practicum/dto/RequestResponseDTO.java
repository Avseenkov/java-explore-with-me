package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestResponseDTO {

    public String app;

    public String uri;

    public Long hits;
}
