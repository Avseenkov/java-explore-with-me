package ru.practicum.main.repository.category;

import ru.practicum.main.model.Category;

public interface CategoryRepositoryCustom {
    Category getCategoryFromDb(Long id);
}
