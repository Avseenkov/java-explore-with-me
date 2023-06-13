package ru.practicum.main.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class CompilationDto {

    private List<EventShortDto> events;

    @NotNull
    private Long id;

    private boolean pinned;

    @NotNull
    private String title;
}
