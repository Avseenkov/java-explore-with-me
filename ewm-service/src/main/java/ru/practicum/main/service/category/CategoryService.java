package ru.practicum.main.service.category;

import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;

import javax.validation.Valid;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(@Valid NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(@Valid NewCategoryDto categoryDto, Long id);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(Long id);

    void deleteCategory(Long id);
}
