package ru.practicum.main.repository.event;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.main.model.Event;

public interface EventRepository extends PagingAndSortingRepository<Event, Long>, QuerydslPredicateExecutor, EventRepositoryCustom {

}
