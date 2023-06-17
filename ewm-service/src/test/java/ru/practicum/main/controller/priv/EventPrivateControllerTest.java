package ru.practicum.main.controller.priv;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.main.config.EwmServiceConfig;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewEventDto;
import ru.practicum.main.dto.UserShortDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.service.event.EventService;
import ru.practicum.main.utils.CreateTestData;
import ru.practicum.main.utils.Settings;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventPrivateController.class)
@Import(value = {EwmServiceConfig.class, Settings.class})
@ImportAutoConfiguration(FeignAutoConfiguration.class)
class EventPrivateControllerTest {

    @MockBean
    EventService eventService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    NewEventDto event;

    EventFullDto eventFullDto;

    EventShortDto eventShortDto;

    UserShortDto userShortDto;

    @BeforeEach
    void setUp() {

        String annotation = "Сплав на байдарках похож на полет.";
        String description = "Сплав на байдарках похож на полет. На спокойной воде — это парение. На бурной, порожистой — выполнение фигур высшего пилотажа. И то, и другое дарят чувство обновления, феерические эмоции, яркие впечатления";

        Float lat = 55.754167f;
        Float lon = 37.62f;

        String title = "Сплав на байдарках";

        event = CreateTestData.createNewEventDto(
                annotation,
                1L,
                description,
                LocalDateTime.of(2023, 10, 1, 22, 10),
                lat,
                lon,
                true,
                10,
                false,
                title
        );

        userShortDto = CreateTestData.createUserShortDto("test", 1L);
        eventFullDto = CreateTestData.createEventFullDto(annotation,
                "test",
                description,
                LocalDateTime.of(2023, 10, 1, 22, 10),
                lat,
                lon,
                true,
                10,
                false,
                title,
                userShortDto,
                "PENDING"
        );

        eventShortDto = CreateTestData.createEventShortDto(annotation,
                "test",
                LocalDateTime.of(2023, 10, 1, 22, 10),
                true,
                title,
                0,
                userShortDto);
    }

    @Test
    void createEvent() throws Exception {

        Mockito.when(eventService.createEvent(Mockito.any(NewEventDto.class), Mockito.anyLong()))
                .thenReturn(eventFullDto);

        mvc.perform(post("/users/{userId}/events", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(event))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", equalTo(event.getTitle())))
                .andExpect(jsonPath("$.state", equalTo("PENDING")))
        ;

    }

    @Test
    void createEventNotFoundUser() throws Exception {
        Mockito.when(eventService.createEvent(Mockito.any(NewEventDto.class), Mockito.anyLong()))
                .thenThrow(new NotFoundException("user not found"));

        mvc.perform(post("/users/{userId}/events", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(event))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEvents() throws Exception {
        List<EventShortDto> events = new ArrayList<>();
        events.add(eventShortDto);
        Mockito.when(eventService.getUserEvents(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(events);
        mvc.perform(get("/users/{userId}/events", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", equalTo(eventShortDto.getTitle())));
    }
}