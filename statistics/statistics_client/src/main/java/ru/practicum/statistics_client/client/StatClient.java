package ru.practicum.statistics_client.client;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.RequestDTO;
import ru.practicum.dto.RequestResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "client", url = "${feign.url}")
@EnableFeignClients
public interface StatClient {

    @RequestMapping(method = RequestMethod.GET, value = "/stats", produces = "application/json")
    List<RequestResponseDTO> getStats(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam String[] uris,
            @RequestParam(defaultValue = "false") boolean unique
    );

    @RequestMapping(method = RequestMethod.GET, value = "/stats", produces = "application/json")
    List<RequestResponseDTO> getStats(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false, defaultValue = "false") boolean unique
    );

    @RequestMapping(method = RequestMethod.GET, value = "/stats", produces = "application/json")
    List<RequestResponseDTO> getStats(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) String[] uris
    );

    @RequestMapping(method = RequestMethod.GET,
            value = "/stats",
            produces = "application/json",
            consumes = "application/json")
    List<RequestResponseDTO> getStats(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end
    );

    @RequestMapping(method = RequestMethod.POST, value = "/stats")
    RequestDTO saveStat(@RequestBody RequestDTO request);

}
