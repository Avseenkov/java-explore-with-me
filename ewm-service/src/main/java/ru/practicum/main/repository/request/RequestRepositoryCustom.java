package ru.practicum.main.repository.request;

import ru.practicum.main.model.Request;

public interface RequestRepositoryCustom {
    Request getRequestFromDb(Long id);
}
