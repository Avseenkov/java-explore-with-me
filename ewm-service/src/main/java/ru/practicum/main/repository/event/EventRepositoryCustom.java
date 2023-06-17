package ru.practicum.main.repository.event;

import ru.practicum.main.model.Event;

public interface EventRepositoryCustom {
    Event getEventFromDb(Long id);
}
