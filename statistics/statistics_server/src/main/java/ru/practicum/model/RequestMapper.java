package ru.practicum.model;

import ru.practicum.dto.RequestDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestMapper {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Request requestFromRequestDTO(RequestDTO requestDTO) {
        Request request = new Request();
        request.setIp(requestDTO.getIp());
        request.setUri(requestDTO.getUri());
        request.setApp(requestDTO.getApp());
        request.setCreatedAt(LocalDateTime.parse(requestDTO.getTimestamp(), format));
        return request;
    }

    public static RequestDTO requestDTOFrom(Request request) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setIp(request.getIp());
        requestDTO.setUri(request.getUri());
        requestDTO.setApp(request.getApp());
        requestDTO.setId(request.getId());
        requestDTO.setTimestamp(request.getCreatedAt().format(format));

        return requestDTO;
    }
}
