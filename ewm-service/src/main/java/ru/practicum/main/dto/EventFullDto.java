package ru.practicum.main.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.model.Location;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EventFullDto {
    @NotNull
    private String annotation;

    @NotNull
    private CategoryDto category;

    private long confirmedRequests;

    private String createdOn;

    private String description;

    @NotNull
    private String eventDate;

    private Long id;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Location location;

    @NotNull
    private boolean paid;

    private long participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    @NotNull
    private String state;

    @NotNull
    private String title;

    private long views;
}
