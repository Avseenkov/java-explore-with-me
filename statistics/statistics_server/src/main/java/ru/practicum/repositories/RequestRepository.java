package ru.practicum.repositories;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.model.Request;

public interface RequestRepository extends CrudRepository<Request, Long>, QuerydslPredicateExecutor<Request> {


}
