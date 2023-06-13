package ru.practicum.main.service.event;

import ru.practicum.main.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

public interface EventService {

    EventFullDto createEvent(@Valid NewEventDto newEventDto, Long userId);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto getEvent(Long userId, Long evenId, HttpServletRequest request);

    EventFullDto getPublicEvent(Long evenId,  HttpServletRequest request);

    List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            boolean onlyAvailable,
            String sort,
            int from,
            int size,
            HttpServletRequest request
    );

    EventFullDto changeEventByAdmin(UpdateEventAdminRequestDto request, Long evenId);

    EventFullDto changeEventByUser(UpdateEventUserRequestDto request, Long userId, Long eventId);

    List<EventFullDto> searchEventByAdmin(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            int from,
            int size
    );

    List<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestsByUser(EventRequestStatusUpdateRequest request, Long userId, Long eventId);
}
