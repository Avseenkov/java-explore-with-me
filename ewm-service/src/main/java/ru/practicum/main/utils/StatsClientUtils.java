package ru.practicum.main.utils;

import ru.practicum.dto.RequestDTO;

import java.time.LocalDateTime;

public class StatsClientUtils {
    public static RequestDTO createRequestDto(String ip, String uri) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setApp(Settings.getNameApp());
        requestDTO.setIp(ip);
        requestDTO.setTimestamp(LocalDateTime.now().format(Settings.getFormatter()));
        requestDTO.setUri(uri);
        return requestDTO;
    }
}
