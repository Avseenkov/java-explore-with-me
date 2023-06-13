package ru.practicum.main.controller.pub;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping("categories")
@AllArgsConstructor
public class CategoryPublicController {

    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(
            @PathVariable Long catId
    ) {
        return categoryService.getCategory(catId);
    }
}
