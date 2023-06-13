package ru.practicum.main.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.main.config.EwmServiceConfig;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.service.category.CategoryService;
import ru.practicum.main.utils.Settings;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryAdminController.class)
@Import(value = {EwmServiceConfig.class, Settings.class})
class CategoryAdminControllerTest {

    @MockBean
    CategoryService categoryService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createCategory() throws Exception {

        NewCategoryDto newCategoryDto = createNewCategoryDto("first");
        CategoryDto categoryDto = createCategoryDto("first", 1L);

        Mockito.when(categoryService.createCategory(Mockito.any(NewCategoryDto.class))).thenReturn(categoryDto);

        mvc.perform(post("/admin/categories")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newCategoryDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("first"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateCategory() throws Exception {
        NewCategoryDto newCategoryDto = createNewCategoryDto("first");
        CategoryDto categoryDto = createCategoryDto("first", 1L);

        Mockito.when(categoryService.updateCategory(Mockito.any(NewCategoryDto.class), Mockito.anyLong()))
                .thenReturn(categoryDto);

        mvc.perform(patch("/admin/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.name", equalTo("first"))
                );
    }

    @Test
    void updateCategoryNotFound() throws Exception {

        NewCategoryDto newCategoryDto = createNewCategoryDto("first");

        Mockito.when(categoryService.updateCategory(Mockito.any(NewCategoryDto.class), Mockito.anyLong()))
                .thenThrow(new NotFoundException("Category not found"));

        mvc.perform(patch("/admin/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("@.message", equalTo("Category not found"))
                );
    }

    @Test
    void updateWrongParam() throws Exception {

        NewCategoryDto newCategoryDto = createNewCategoryDto("first");

        mvc.perform(patch("/admin/categories/{id}", -1)
                        .content(objectMapper.writeValueAsString(newCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void createWrongName() throws Exception {

        NewCategoryDto newCategoryDto = createNewCategoryDto("");

        mvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newCategoryDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("name: size must be between 1 and 50")));
    }

    @Test
    void deleteCategory() throws Exception {
        Mockito.doThrow(NotFoundException.class).when(categoryService).deleteCategory(Mockito.anyLong());

        mvc.perform(delete("/admin/categories/{id}", 1L))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteCategoryWrongParameter() throws Exception {
        mvc.perform(delete("/admin/categories/{id}", -1))
                .andExpect(status().isConflict());
    }

    private NewCategoryDto createNewCategoryDto(String name) {
        NewCategoryDto newCategoryDto = new NewCategoryDto();
        newCategoryDto.setName(name);
        return newCategoryDto;
    }

    private CategoryDto createCategoryDto(String name, Long id) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        categoryDto.setId(id);
        return categoryDto;
    }

}