package ru.practicum.services;

import ru.practicum.dto.RequestDTO;
import ru.practicum.dto.RequestResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestService {

    RequestDTO save(RequestDTO requestDTO);

    List<RequestResponseDTO> getStat(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
