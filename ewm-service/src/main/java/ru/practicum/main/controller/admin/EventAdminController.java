package ru.practicum.main.controller.admin;


import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.UpdateEventAdminRequestDto;
import ru.practicum.main.service.event.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin")
@AllArgsConstructor
@Validated
public class EventAdminController {

    private EventService eventService;

    @PatchMapping("/events/{eventId}")
    public EventFullDto changeEventByAdmin(
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventAdminRequestDto requestDto
    ) {
        return eventService.changeEventByAdmin(requestDto, eventId);
    }

    @GetMapping("/events")
    public List<EventFullDto> searchEventByAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventService.searchEventByAdmin(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size
        );
    }

}
