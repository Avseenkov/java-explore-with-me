package ru.practicum.main.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long>, QuerydslPredicateExecutor, RequestRepositoryCustom {

    Optional<Request> findByEvent_IdAndRequester_Id(Long eventId, Long userId);

    List<Request> findAllByRequester_Id(Long userId);

    @Query("select r from Request r where r.event.id = ?1 and r.event.initiator.id = ?2")
    List<Request> findByEvent_IdAndEvent_Initiator_Id(Long id, Long id1);
}
