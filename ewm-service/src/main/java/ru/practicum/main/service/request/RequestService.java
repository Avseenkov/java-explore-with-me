package ru.practicum.main.service.request;

import ru.practicum.main.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long id);

    ParticipationRequestDto setCanceled(Long userId, Long requestId);
}
