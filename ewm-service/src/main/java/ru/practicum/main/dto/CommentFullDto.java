package ru.practicum.main.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentFullDto {

    @NotNull
    private Long id;

    @NotBlank
    private String text;

    @NotNull
    private UserShortDto user;

    @NotNull
    private EventFullDto event;
}
