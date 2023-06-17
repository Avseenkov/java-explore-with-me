package ru.practicum.main.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class NewUserRequestDto {

    @NotNull
    @Email
    @Size(min = 6, max = 254)
    private String email;

    @Size(min = 2, max = 250)
    @NotBlank
    private String name;
}
