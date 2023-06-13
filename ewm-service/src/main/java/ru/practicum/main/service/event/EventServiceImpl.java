package ru.practicum.main.service.event;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.main.dto.*;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.mapper.RequestMapper;
import ru.practicum.main.model.*;
import ru.practicum.main.repository.category.CategoryRepository;
import ru.practicum.main.repository.event.EventRepository;
import ru.practicum.main.repository.request.RequestRepository;
import ru.practicum.main.repository.user.UserRepository;
import ru.practicum.main.service.stats.StatsService;
import ru.practicum.main.utils.Settings;
import ru.practicum.main.utils.StatsClientUtils;
import ru.practicum.statistics_client.client.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Validated
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final StatsService statsService;

    private final StatClient statClient;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        User user = userRepository.getUserFromDb(userId);
        Category category = categoryRepository.getCategoryFromDb(newEventDto.getCategory());
        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(user);
        event.setCategory(category);
        event.setState(State.PENDING);
        if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Date event is wrong");
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        userRepository.getUserFromDb(userId);
        int page = from / size;

        Sort sortBy = Sort.by(Sort.Direction.DESC, "eventDate");
        PageRequest pageRequest = PageRequest.of(page, size, sortBy);
        Iterable<Event> events = eventRepository.findAll(QEvent.event.initiator.id.eq(userId), pageRequest);
        return EventMapper.mapToEventShortDto(events);
    }

    @Override
    @Transactional
    public EventFullDto getEvent(Long userId, Long evenId, HttpServletRequest request) {
        userRepository.getUserFromDb(userId);

        return EventMapper.toEventFullDto(eventRepository.getEventFromDb(evenId));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getPublicEvent(Long evenId, HttpServletRequest request) {
        Event event = eventRepository.getEventFromDb(evenId);
        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Event not found");
        }

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);

        Map<Long, Long> views = statsService.getViews(List.of(event), true);

        EventMapper.setView(eventFullDto, views);

        statClient.saveStat(StatsClientUtils.createRequestDto(request.getRemoteAddr(), request.getRequestURI()));

        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto changeEventByAdmin(UpdateEventAdminRequestDto request, Long evenId) {
        Event event = eventRepository.getEventFromDb(evenId);
        if (request.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(request.getEventDate(), Settings.getFormatter());

            if (newEventDate.plusHours(1).isBefore(event.getEventDate())) {
                throw new BadRequestException("Forbidden to change event because of event time");
            }

            event.setEventDate(newEventDate);
        }

        if (request.getStateAction() != null) {
            if (request.getStateAction().equals("PUBLISH_EVENT") && event.getState() != State.PENDING) {
                throw new ConflictException("Event can`t be published");
            }

            if (request.getStateAction().equals("REJECT_EVENT") && event.getState() != State.PENDING) {
                throw new ConflictException("Event can`t be canceled");
            }

        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getCategory() != null) {
            Category newCategory = categoryRepository.getCategoryFromDb(request.getCategory());
            event.setCategory(newCategory);
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case "PUBLISH_EVENT": {
                    event.setState(State.PUBLISHED);
                    break;
                }
                case "REJECT_EVENT": {
                    event.setState(State.CANCELED);
                    break;
                }
                default:
                    throw new BadRequestException("Wrong state action");
            }
        }

        Event newEvent = eventRepository.save(event);

        Map<Long, Long> views = statsService.getViews(List.of(newEvent), true);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(newEvent);

        EventMapper.setView(eventFullDto, views);

        return eventFullDto;
    }

    @Override
    public EventFullDto changeEventByUser(UpdateEventUserRequestDto request, Long userId, Long eventId) {
        User user = userRepository.getUserFromDb(userId);
        Event event = eventRepository.getEventFromDb(eventId);

        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new NotFoundException("Event not found");
        }

        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("Event must being waiting publish or canceled");
        }

        if (request.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(request.getEventDate(), Settings.getFormatter());

            if (newEventDate.plusHours(2).isBefore(event.getEventDate())) {
                throw new BadRequestException("Forbidden to change event because of event time");
            }

            event.setEventDate(newEventDate);
        }


        if (request.getStateAction() != null) {
            if (request.getStateAction().equals("REJECT_EVENT") && event.getState() == State.CANCELED) {
                throw new ConflictException("Event is canceled yet");
            }
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getCategory() != null) {
            Category newCategory = categoryRepository.getCategoryFromDb(request.getCategory());
            event.setCategory(newCategory);
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case "SEND_TO_REVIEW": {
                    event.setState(State.PENDING);
                    break;
                }
                case "CANCEL_REVIEW": {
                    event.setState(State.CANCELED);
                    break;
                }
                default:
                    throw new BadRequestException("Wrong state action");
            }
        }

        Event newEvent = eventRepository.save(event);

        Map<Long, Long> views = statsService.getViews(List.of(newEvent), true);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(newEvent);

        EventMapper.setView(eventFullDto, views);

        return eventFullDto;

    }

    @Override
    public List<EventShortDto> getEvents(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         boolean onlyAvailable,
                                         String sort,
                                         int from,
                                         int size,
                                         HttpServletRequest request) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(QEvent.event.state.eq(State.PUBLISHED));

        if (text != null) {
            builder.and(QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text))
            );
        }

        if (categories != null) {
            builder.and(QEvent.event.category.id.in(categories));
        }

        if (paid != null) {
            builder.and(QEvent.event.paid.eq(paid));
        }

        if (rangeStart != null && rangeEnd != null) {

            if (LocalDateTime.parse(rangeEnd, Settings.getFormatter())
                    .isBefore(LocalDateTime.parse(rangeStart, Settings.getFormatter()))) {
                throw new BadRequestException("End date should be after start date");
            }

            builder.and(QEvent.event.eventDate.between(
                            LocalDateTime.parse(rangeStart, Settings.getFormatter()),
                            LocalDateTime.parse(rangeEnd, Settings.getFormatter())
                    )
            );

        } else {
            builder.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }

        if (onlyAvailable) {
            builder.and(
                    QEvent.event.participantLimit.lt(QEvent.event.confirmedRequests)
            );
        }

        int page = from / size;

        PageRequest pageRequest = PageRequest.of(page, size);

        if (Objects.equals(sort, "EVENT_DATE")) {
            pageRequest.withSort(Sort.by(Sort.Direction.DESC, "eventDate"));
        }

        Iterable<Event> events = eventRepository.findAll(builder.getValue(), pageRequest);

        List<EventShortDto> eventsShortDto = EventMapper.mapToEventShortDto(events);

        Map<Long, Long> views = statsService.getViews(Lists.newArrayList(events), true);

        EventMapper.setViews(eventsShortDto, views);

        statClient.saveStat(StatsClientUtils.createRequestDto(request.getRemoteAddr(), request.getRequestURI()));

        return eventsShortDto;
    }


    @Override
    public List<EventFullDto> searchEventByAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size) {

        BooleanBuilder builder = new BooleanBuilder();
        if (users != null) {
            builder.and(QEvent.event.initiator.id.in(users));
        }
        if (states != null) {
            List<State> stateList = new ArrayList<>();
            states.stream().forEach(s -> stateList.add(State.valueOf(s)));
            builder.and(QEvent.event.state.in(stateList));
        }

        if (categories != null) {
            builder.and(QEvent.event.category.id.in(categories));
        }

        if (rangeStart != null) {
            builder.and(QEvent.event.eventDate.after(LocalDateTime.parse(rangeStart, Settings.getFormatter())));
        } else
            builder.and(QEvent.event.eventDate.after(LocalDateTime.of(2000, 1, 1, 0, 0)));

        if (rangeEnd != null) {
            builder.and(QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, Settings.getFormatter())));
        }
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        Iterable<Event> events = eventRepository.findAll(builder.getValue(), pageRequest);

        List<EventFullDto> eventFullDtos = EventMapper.mapToEventFullDto(events);

        Map<Long, Long> views = statsService.getViews(Lists.newArrayList(events), true);

        EventMapper.setViewsToListEventFullDto(eventFullDtos, views);

        return eventFullDtos;

    }

    @Override
    public List<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId) {
        User user = userRepository.getUserFromDb(userId);
        Event event = eventRepository.getEventFromDb(eventId);
        List<Request> requests = requestRepository.findByEvent_IdAndEvent_Initiator_Id(
                event.getId(),
                user.getId()
        );
        return RequestMapper.mapToParticipationRequestDto(requests);
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestsByUser(EventRequestStatusUpdateRequest request, Long userId, Long eventId) {
        User user = userRepository.getUserFromDb(userId);
        Event event = eventRepository.getEventFromDb(eventId);
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QRequest.request.id.in(request.getRequestIds()));
        builder.and(QRequest.request.event.requestModeration.eq(true));
        Iterable<Request> requests = requestRepository.findAll(
                builder.getValue()
        );

        Set<Long> eventWithLimit = new HashSet<>();

        requests.forEach(r -> {

            if (r.getStatus() != Status.PENDING) {
                throw new ConflictException("Request must have status PENDING");
            }
            if (r.getEvent().getState() != State.PUBLISHED) {
                throw new ConflictException("Event must have status PUBLISHED");
            }

            if (request.getStatus().equals("REJECTED")) {
                r.setStatus(Status.REJECTED);
            } else {

                if (eventWithLimit.contains(r.getEvent().getId())) {
                    r.setStatus(Status.REJECTED);
                } else {
                    long countRequest = r.getEvent().getConfirmedRequests();
                    if (countRequest >= r.getEvent().getParticipantLimit()) {
                        throw new ConflictException("The participant limit has been reached");
                    }

                    r.setStatus(Status.CONFIRMED);
                    if (++countRequest >= r.getEvent().getConfirmedRequests()) {
                        eventWithLimit.add(r.getEvent().getId());
                    }
                }
            }
        });

        requests = requestRepository.saveAll(requests);

        return RequestMapper.toEventRequestStatusUpdateResult(Lists.newArrayList(requests));
    }
}
