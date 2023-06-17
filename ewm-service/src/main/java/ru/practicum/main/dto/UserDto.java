package ru.practicum.main.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDto {

    @NotNull
    @Email
    private String email;

    private Long id;

    private String name;
}
