package ru.practicum.main.repository.user;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.main.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, QuerydslPredicateExecutor, UserRepositoryCustom {


}
