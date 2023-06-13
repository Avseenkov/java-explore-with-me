package ru.practicum.main.repository.compilation;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.main.model.Compilation;

public interface CompilationRepository extends PagingAndSortingRepository<Compilation, Long>, QuerydslPredicateExecutor<Compilation>, CompilationRepositoryCustom {
}
