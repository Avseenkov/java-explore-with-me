package ru.practicum.main.mapper;

import ru.practicum.main.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Request;
import ru.practicum.main.model.Status;
import ru.practicum.main.model.User;
import ru.practicum.main.utils.Settings;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(Settings.getFormatter().format(request.getCreated()));
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus().name());
        return requestDto;
    }

    public static Request makeRequest(User user, Event event) {
        Request request = new Request();
        request.setRequester(user);
        request.setEvent(event);
        request.setStatus(Status.PENDING);
        return request;
    }

    public static List<ParticipationRequestDto> mapToParticipationRequestDto(List<Request> requests) {
        List<ParticipationRequestDto> requestDtos = new ArrayList<>();
        for (Request request : requests) {
            requestDtos.add(toParticipationRequestDto(request));
        }
        return requestDtos;
    }

    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<Request> requests) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        requests.stream().forEach(request -> {
            if (request.getStatus() == Status.REJECTED) {
                rejectedRequests.add(toParticipationRequestDto(request));
            } else {
                confirmedRequests.add(toParticipationRequestDto(request));
            }
        });

        return result;
    }
}
