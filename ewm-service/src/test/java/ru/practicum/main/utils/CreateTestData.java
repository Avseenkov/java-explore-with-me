package ru.practicum.main.utils;

import ru.practicum.main.dto.*;
import ru.practicum.main.model.*;

import java.time.LocalDateTime;

public class CreateTestData {
    public static User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    public static UserShortDto createUserShortDto(String name, Long id) {
        UserShortDto user = new UserShortDto();
        user.setName(name);
        user.setId(id);
        return user;
    }

    public static Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

    public static CategoryDto createCategoryDto(String name) {
        CategoryDto category = new CategoryDto();
        category.setName(name);
        return category;
    }

    public static Event createEvent(
            String annotation,
            Category category,
            String description,
            LocalDateTime eventDate,
            Float lat,
            Float lon,
            boolean paid,
            int participantLimit,
            boolean requestModeration,
            String title,
            User initiator,
            State state
    ) {
        Event event = new Event();
        event.setAnnotation(annotation);
        event.setCategory(category);
        event.setDescription(description);
        event.setEventDate(eventDate);
        Location location = new Location();
        location.setLon(lon);
        location.setLat(lat);
        event.setLocation(location);
        event.setPaid(paid);
        event.setParticipantLimit(participantLimit);
        event.setRequestModeration(requestModeration);
        event.setTitle(title);
        event.setInitiator(initiator);
        event.setState(state);
        return event;
    }

    public static NewEventDto createNewEventDto(
            String annotation,
            Long category,
            String description,
            LocalDateTime eventDate,
            Float lat,
            Float lon,
            boolean paid,
            int participantLimit,
            boolean requestModeration,
            String title) {
        NewEventDto event = new NewEventDto();
        event.setAnnotation(annotation);
        event.setCategory(category);
        event.setDescription(description);
        event.setEventDate(Settings.getFormatter().format(eventDate));
        Location location = new Location();
        location.setLon(lon);
        location.setLat(lat);
        event.setLocation(location);
        event.setPaid(paid);
        event.setParticipantLimit(participantLimit);
        event.setRequestModeration(requestModeration);
        event.setTitle(title);
        return event;
    }

    public static EventFullDto createEventFullDto(
            String annotation,
            String category,
            String description,
            LocalDateTime eventDate,
            Float lat,
            Float lon,
            boolean paid,
            int participantLimit,
            boolean requestModeration,
            String title,
            UserShortDto user,
            String state
    ) {
        EventFullDto event = new EventFullDto();
        event.setAnnotation(annotation);
        event.setCategory(createCategoryDto(category));
        event.setDescription(description);
        event.setEventDate(Settings.getFormatter().format(eventDate));
        Location location = new Location();
        location.setLon(lon);
        location.setLat(lat);
        event.setLocation(location);
        event.setPaid(paid);
        event.setParticipantLimit(participantLimit);
        event.setRequestModeration(requestModeration);
        event.setTitle(title);
        event.setInitiator(user);
        event.setState(state);
        return event;
    }

    public static EventShortDto createEventShortDto(
            String annotation,
            String category,
            LocalDateTime eventDate,
            boolean paid,
            String title,
            long confirmedRequests,
            UserShortDto user
    ) {
        EventShortDto event = new EventShortDto();
        event.setAnnotation(annotation);
        event.setCategory(createCategoryDto(category));
        event.setEventDate(Settings.getFormatter().format(eventDate));
        event.setPaid(paid);
        event.setTitle(title);
        event.setConfirmedRequests(confirmedRequests);
        event.setInitiator(user);
        return event;
    }

}
