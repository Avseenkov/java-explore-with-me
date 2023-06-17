package ru.practicum.main.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.model.Location;

import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateEventAdminRequestDto {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String stateAction;

    @Size(min = 3, max = 120)
    private String title;

}
