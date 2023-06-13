package ru.practicum.main.controller.priv;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.RequestService;

import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class RequestPrivateController {

    private RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(
            @PathVariable Long userId
    ) {
        return requestService.getRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto setCanceled(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        return requestService.setCanceled(userId, requestId);
    }


}
