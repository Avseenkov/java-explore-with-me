package ru.practicum.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.RequestResponseDTO;
import ru.practicum.statistics_client.client.StatClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Test {

    @Autowired
    private StatClient statClient;

    public List<RequestResponseDTO> getStat() {
        List<RequestResponseDTO> result = statClient.getStats(
                LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0)
        );

        return result;
    }
}
