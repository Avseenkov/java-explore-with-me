package ru.practicum.main.service.event;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewEventDto;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.State;
import ru.practicum.main.model.User;
import ru.practicum.main.utils.CreateTestData;
import ru.practicum.statistics_client.client.StatClient;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EventServiceImplTest {

    @Autowired
    EventService eventService;

    @Mock
    StatClient statClient;

    @Autowired
    EntityManager em;

    @Test
    void createEvent() {
        User user = CreateTestData.createUser("test", "test@test.ru");
        Category category = CreateTestData.createCategory("event");

        em.persist(user);
        em.persist(category);
        em.flush();

        String annotation = "Сплав на байдарках похож на полет.";
        String description = "Сплав на байдарках похож на полет. На спокойной воде — это парение. На бурной, порожистой — выполнение фигур высшего пилотажа. И то, и другое дарят чувство обновления, феерические эмоции, яркие впечатления";

        Float lat = 55.754167f;
        Float lon = 37.62f;

        String title = "Сплав на байдарках";

        NewEventDto event = CreateTestData.createNewEventDto(
                annotation,
                category.getId(),
                description,
                LocalDateTime.of(2023, 10, 1, 22, 10),
                lat,
                lon,
                true,
                10,
                false,
                title
        );

        eventService.createEvent(event, user.getId());

        TypedQuery<Event> query = em.createQuery("SELECT E FROM Event E WHERE E.title = :title", Event.class);
        Event eventFromDb = query.setParameter("title", event.getTitle()).getSingleResult();

        assertThat(eventFromDb.getLocation().getLon(), equalTo(event.getLocation().getLon()));
        assertThat(eventFromDb.getLocation().getLat(), equalTo(event.getLocation().getLat()));
        assertThat(eventFromDb.getInitiator().getName(), equalTo(user.getName()));
        assertThat(eventFromDb.getConfirmedRequests(), equalTo(0L));
        assertThat(eventFromDb.getState(), equalTo(State.PENDING));
    }


    @Test
    void getUserEvents() {

        User user = CreateTestData.createUser("test", "test@test.ru");
        User user1 = CreateTestData.createUser("test1", "test1@test.ru");
        Category category = CreateTestData.createCategory("event");

        em.persist(user);
        em.persist(category);
        em.persist(user1);
        em.flush();

        List<Event> events = getEvents();

        for (Event event : events) {
            event.setCategory(category);
            event.setInitiator(user);
            em.persist(event);
        }

        events = getEvents();

        for (Event event : events) {
            event.setCategory(category);
            event.setInitiator(user1);
            em.persist(event);
        }

        em.flush();

        List<EventShortDto> eventsFromDb = eventService.getUserEvents(user.getId(), 0, 3);
        assertThat(eventsFromDb, hasSize(2));

    }

    List<Event> getEvents() {
        List<Event> events = new ArrayList<>();

        String annotation = "Сплав на байдарках похож на полет.";
        String description = "Сплав на байдарках похож на полет. На спокойной воде — это парение. На бурной, порожистой — выполнение фигур высшего пилотажа. И то, и другое дарят чувство обновления, феерические эмоции, яркие впечатления";
        Float lat = 55.754167f;
        Float lon = 37.62f;
        String title = "Сплав на байдарках";

        Event event = CreateTestData.createEvent(
                annotation,
                null,
                description,
                LocalDateTime.of(2023, 10, 1, 22, 10),
                lat,
                lon,
                true,
                10,
                false,
                title,
                null,
                State.PENDING
        );

        events.add(event);
        Event event1 = CreateTestData.createEvent(
                annotation,
                null,
                description,
                LocalDateTime.of(2023, 11, 1, 22, 10),
                lat,
                lon,
                true,
                10,
                false,
                title,
                null,
                State.PENDING
        );

        events.add(event1);

        return events;
    }

    @Test
    void getEvent() {
        User user = CreateTestData.createUser("test", "test@test.ru");
        Category category = CreateTestData.createCategory("event");

        em.persist(user);
        em.persist(category);

        em.flush();

        List<Event> events = getEvents();

        for (Event event : events) {
            event.setCategory(category);
            event.setInitiator(user);
            em.persist(event);
        }

        EventFullDto eventFullDto = eventService.getEvent(user.getId(), events.get(0).getId());

        assertThat(eventFullDto.getId(), equalTo(events.get(0).getId()));
        assertThat(eventFullDto.getTitle(), equalTo(events.get(0).getTitle()));
    }
}