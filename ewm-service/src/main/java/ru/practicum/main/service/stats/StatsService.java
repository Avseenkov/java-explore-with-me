package ru.practicum.main.service.stats;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.RequestResponseDTO;
import ru.practicum.main.model.Event;
import ru.practicum.statistics_client.client.StatClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatsService {

    private StatClient statClient;

    public Map<Long, Long> getViews(List<Event> events, boolean unique) {

        Map<Long, Long> views = new HashMap<>();

        if (events.isEmpty()) {
            return views;
        }

        Optional<LocalDateTime> minDate = events.stream().map(Event::getPublishedOn).min(LocalDateTime::compareTo);

        if (!minDate.isPresent()) {
            return views;
        }

        List<String> urls = events.stream().map(event -> String.format("/events/%s", event.getId())).collect(Collectors.toList());


        List<RequestResponseDTO> response = statClient.getStats(minDate.get(), LocalDateTime.now(), urls, unique);

        response.stream().forEach(requestResponseDTO -> views.put(Long.parseLong(requestResponseDTO.getUri().split("/events/")[1]), requestResponseDTO.getHits()));
        return views;
    }
}
