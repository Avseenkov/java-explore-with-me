package ru.practicum.main.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
public class CommentShortDto {

    @NotNull
    private Long id;

    @NotBlank
    private String text;

    @NotNull
    private String authorName;

    @NotNull
    private String createdAt;

}
