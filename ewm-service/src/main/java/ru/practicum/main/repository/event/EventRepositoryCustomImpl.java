package ru.practicum.main.repository.event;

import org.springframework.context.annotation.Lazy;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Event;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final EventRepository eventRepository;

    public EventRepositoryCustomImpl(@Lazy EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event getEventFromDb(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException("event not found"));
    }
}
