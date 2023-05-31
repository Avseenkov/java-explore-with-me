package ru.practicum.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.RequestDTO;
import ru.practicum.dto.RequestResponseDTO;
import ru.practicum.services.RequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class RequestController {

    RequestService requestService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDTO addRequest(@RequestBody RequestDTO request) {
        return requestService.save(request);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponseDTO> getStat(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return requestService.getStat(start, end, uris, unique);
    }
}
