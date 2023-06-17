package ru.practicum.main.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Setter
@Getter
public class Location {

    private Float lat;

    private Float lon;

}
