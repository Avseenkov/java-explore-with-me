package ru.practicum.main.service.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.RequestMapper;
import ru.practicum.main.model.*;
import ru.practicum.main.repository.event.EventRepository;
import ru.practicum.main.repository.request.RequestRepository;
import ru.practicum.main.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;

    private UserRepository userRepository;

    private EventRepository eventRepository;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.getUserFromDb(userId);
        Event event = eventRepository.getEventFromDb(eventId);

        Optional<Request> request = requestRepository.findByEvent_IdAndRequester_Id(eventId, userId);

        if (request.isPresent()) {
            throw new ConflictException("Repeated request");
        }

        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Owner of event cannot make request");
        }

        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Wrong state of event");
        }

        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ConflictException("Reached limit");
        }

        Request requestDb = RequestMapper.makeRequest(user, event);

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            requestDb.setStatus(Status.CONFIRMED);
        }

        return RequestMapper.toParticipationRequestDto(
                requestRepository.save(requestDb)
        );
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long id) {
        User user = userRepository.getUserFromDb(id);

        return RequestMapper.mapToParticipationRequestDto(
                requestRepository.findAllByRequester_Id(id)
        );
    }

    @Override
    public ParticipationRequestDto setCanceled(Long userId, Long requestId) {
        User user = userRepository.getUserFromDb(userId);
        Request request = requestRepository.getRequestFromDb(requestId);
        if (!request.getRequester().getId().equals(user.getId())) {
            throw new NotFoundException("Request not found");
        }
        request.setStatus(Status.CANCELED);

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}
