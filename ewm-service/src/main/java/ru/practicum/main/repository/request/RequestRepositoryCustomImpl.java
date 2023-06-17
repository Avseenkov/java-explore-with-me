package ru.practicum.main.repository.request;

import org.springframework.context.annotation.Lazy;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Request;

public class RequestRepositoryCustomImpl implements RequestRepositoryCustom {

    private final RequestRepository requestRepository;

    public RequestRepositoryCustomImpl(@Lazy RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Request getRequestFromDb(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Request not found"));
    }
}
