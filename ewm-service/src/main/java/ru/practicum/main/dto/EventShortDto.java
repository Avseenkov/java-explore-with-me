package ru.practicum.main.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class EventShortDto {

    @NotNull
    private String annotation;

    @NotNull
    private CategoryDto category;

    private long confirmedRequests;

    @NotNull
    private String eventDate;

    private Long id;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private boolean paid;

    @NotNull
    private String title;

    private long views;
}
