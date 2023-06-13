package ru.practicum.main.service.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.mapper.CategoryMapper;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.QCategory;
import ru.practicum.main.model.QEvent;
import ru.practicum.main.repository.category.CategoryRepository;
import ru.practicum.main.repository.event.EventRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategory(@Valid NewCategoryDto newCategoryDto) {

        Optional<Category> existCategory = categoryRepository.findOne(
                QCategory.category.name.eq(newCategoryDto.getName()));
        if (existCategory.isPresent()) {
            throw new ConflictException("Category with updated name is exist");
        }
        return CategoryMapper.toCategoryDto(
                categoryRepository.save(CategoryMapper.toCategory(newCategoryDto))
        );
    }

    @Override
    public CategoryDto updateCategory(NewCategoryDto categoryDto, Long id) {
        CategoryDto category = CategoryMapper.toCategoryDto(categoryRepository.getCategoryFromDb(id));
        category.setName(categoryDto.getName());
        Optional<Category> existCategory = categoryRepository.findOne(
                QCategory.category.name.eq(categoryDto.getName())
                        .and(QCategory.category.id.ne(category.getId())));
        if (existCategory.isPresent()) {
            throw new ConflictException("Category with updated name is exist");
        }
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(category)));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.getCategoryFromDb(id);

        Optional<Event> event = eventRepository.findOne(QEvent.event.category.id.eq(category.getId()));
        if (event.isPresent()) {
            throw new ConflictException("The category is not empty");
        }
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {

        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Iterable<Category> categories = categoryRepository.findAll(pageRequest);
        return CategoryMapper.mapToCategoryDto(categories);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        return CategoryMapper.toCategoryDto(categoryRepository.getCategoryFromDb(id));
    }
}
