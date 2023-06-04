package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestResponseDTO {

    private String app;

    private String uri;

    private Long hits;
}
