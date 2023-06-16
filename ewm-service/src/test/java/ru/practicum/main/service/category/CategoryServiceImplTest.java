package ru.practicum.main.service.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Category;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryServiceImplTest {


    NewCategoryDto newCategoryDto;

    @Autowired
    EntityManager em;

    @Autowired
    CategoryService categoryService;

    @BeforeEach
    void setUp() {
        newCategoryDto = createNewCategoryDto("test");
    }

    @Test
    void createCategory() {

        CategoryDto categoryDto = categoryService.createCategory(newCategoryDto);

        assertThat(categoryDto.getName(), equalTo(newCategoryDto.getName()));
        assertThat(categoryDto.getId(), notNullValue());

        TypedQuery<Category> query = em.createQuery("SELECT C FROM Category C WHERE C.name = :name", Category.class);
        Category categoryDb = query.setParameter("name", categoryDto.getName()).getSingleResult();

        assertThat(categoryDb.getName(), equalTo(categoryDto.getName()));
    }

    @Test
    void createCategoryWrongName() {
        NewCategoryDto newCategory = createNewCategoryDto(getLongName(51));

        assertThrows(ConstraintViolationException.class, () ->
                categoryService.createCategory(newCategory)
        );

    }

    @Test
    void updateCategory() {

        Category category = createCategory("first");
        em.persist(category);
        em.flush();

        NewCategoryDto newCategory = createNewCategoryDto("second");

        CategoryDto categoryDto = categoryService.updateCategory(newCategory, category.getId());

        TypedQuery<Category> query = em.createQuery("select C from Category C where C.id = :id", Category.class);

        Category result = query.setParameter("id", categoryDto.getId()).getSingleResult();

        assertThat(result.getName(), equalTo(newCategory.getName()));

    }

    @Test
    void updateEmptyName() {
        Category category = createCategory("first");
        em.persist(category);
        em.flush();

        NewCategoryDto newCategory = createNewCategoryDto(null);

        assertThrows(ConstraintViolationException.class, () -> {
            categoryService.updateCategory(newCategory, category.getId());
        });
    }

    @Test
    void updateNotExistCategory() {
        NewCategoryDto newCategory = createNewCategoryDto("test");
        assertThrows(NotFoundException.class, () ->
                categoryService.updateCategory(newCategory, 22L));
    }

    NewCategoryDto createNewCategoryDto(String name) {
        NewCategoryDto newCategoryDto = new NewCategoryDto();
        newCategoryDto.setName(name);
        return newCategoryDto;
    }

    @Test
    void deleteCategoryNotFound() {
        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1L));
    }

    @Test
    void deleteCategory() {
        Category category = createCategory("first");
        em.persist(category);
        em.flush();

        categoryService.deleteCategory(category.getId());

        List<Category> categories = em.createQuery("SELECT C FROM Category C").getResultList();

        assertThat(categories, hasSize(0));
    }

    Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

    String getLongName(int size) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i <= size; i++) {
            sb.append("a");
        }

        return sb.toString();
    }


}