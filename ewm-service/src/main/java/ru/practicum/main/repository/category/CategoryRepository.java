package ru.practicum.main.repository.category;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.main.model.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long>, QuerydslPredicateExecutor<Category>, CategoryRepositoryCustom {
}
