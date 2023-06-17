package ru.practicum.main.repository.category;

import org.springframework.context.annotation.Lazy;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Category;

public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

    private final CategoryRepository categoryRepository;

    public CategoryRepositoryCustomImpl(@Lazy CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryFromDb(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category with %s not found", id)));
    }
}
