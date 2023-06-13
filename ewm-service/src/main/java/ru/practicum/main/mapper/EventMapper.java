package ru.practicum.main.mapper;

import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewEventDto;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Location;
import ru.practicum.main.utils.Settings;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), Settings.getFormatter()));
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setTitle(newEventDto.getTitle());
        return event;
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(Settings.getFormatter().format(event.getPublishedOn()));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(Settings.getFormatter().format(event.getEventDate()));
        eventFullDto.setId(event.getId());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPublishedOn(Settings.getFormatter().format(event.getPublishedOn()));
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState().toString());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());

        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(Settings.getFormatter().format(event.getEventDate()));
        eventShortDto.setId(event.getId());
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setPaid(event.isPaid());

        return eventShortDto;

    }

    public static void setViews(List<EventShortDto> events, Map<Long, Long> views) {

        events.stream()
                .forEach(event -> event.setViews(views.getOrDefault(event.getId(), 0L)));

    }

    public static void setViewsToListEventFullDto(List<EventFullDto> events, Map<Long, Long> views) {

        events.stream()
                .forEach(event -> event.setViews(views.getOrDefault(event.getId(), 0L)));

    }

    public static void setView(EventFullDto event, Map<Long, Long> views) {
        event.setViews(views.getOrDefault(event.getId(), 0L));
    }

    private static Location getLocation(Float lat, Float lon) {
        Location location = new Location();
        location.setLat(lat);
        location.setLon(lon);
        return location;
    }


    public static List<EventFullDto> mapToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        for (Event event : events) {
            eventFullDtos.add(toEventFullDto(event));
        }
        return eventFullDtos;
    }

    public static List<EventShortDto> mapToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event event : events) {
            eventShortDtos.add(toEventShortDto(event));
        }
        return eventShortDtos;
    }
}
