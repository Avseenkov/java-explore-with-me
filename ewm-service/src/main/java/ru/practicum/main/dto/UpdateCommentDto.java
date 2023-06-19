package ru.practicum.main.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class UpdateCommentDto {
    @NotBlank
    @Size(min = 2)
    private String text;
}
