package ru.practicum.main.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.main.config.EwmServiceConfig;
import ru.practicum.main.dto.NewUserRequestDto;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.service.user.UserService;
import ru.practicum.main.utils.Settings;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAdminController.class)
@Import(value = {EwmServiceConfig.class, Settings.class})
class UserAdminControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mvc;

    NewUserRequestDto newUser;

    UserDto userDto;


    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {

        newUser = new NewUserRequestDto();
        newUser.setEmail("test@test.com");
        newUser.setName("test_test");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setName("test_test");
    }

    @Test
    void createUser() throws Exception {
        Mockito.when(userService.createUser(Mockito.any(NewUserRequestDto.class)))
                .thenReturn(userDto);

        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(newUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(userDto)));
    }

    @Test
    void createWithoutEmail() throws Exception {
        NewUserRequestDto user = new NewUserRequestDto();
        user.setName("test");
        user.setEmail("    ");
        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("email: must be a well-formed email address"))
                );
    }

    @Test
    void createWrongName() throws Exception {
        NewUserRequestDto user = new NewUserRequestDto();
        user.setName("t");
        user.setEmail("test@test.ru");
        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("name: size must be between 2 and 250"))
                );
    }

    @Test
    void createWrongEmail() throws Exception {
        NewUserRequestDto user = new NewUserRequestDto();
        user.setName("test");
        user.setEmail("testest");
        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("email: must be a well-formed email address"))
                );
    }

    @Test
    void getUsers() throws Exception {

        List<UserDto> userDtos = List.of(
                createUserDto("test", "test@test.ru", 1L),
                createUserDto("test2", "test2@test.ru", 2L),
                createUserDto("test3", "test3@test.ru", 3L)
        );

        Mockito.when(userService.findUsers(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
                .thenReturn(userDtos);

        MvcResult mvcResult = mvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();

        List<UserDto> result = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<UserDto>>() {
        });

        for (UserDto dto : userDtos) {
            assertThat(result, hasItem(anyOf(
                    hasProperty("id", equalTo(dto.getId())),
                    hasProperty("name", equalTo(dto.getName())),
                    hasProperty("email", equalTo(dto.getEmail()))
            )));
        }
    }


    @Test
    void getUsersWithIds() throws Exception {

        List<UserDto> userDtos = List.of(
                createUserDto("test", "test@test.ru", 1L),
                createUserDto("test2", "test2@test.ru", 2L)
        );

        Mockito.when(userService.findUsers(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList()))
                .thenReturn(userDtos);

        MvcResult mvcResult = mvc.perform(get("/admin/users")
                        .param("ids", "1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

        List<UserDto> result = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<UserDto>>() {
        });

        for (UserDto dto : userDtos) {
            assertThat(result, hasItem(anyOf(
                    hasProperty("id", equalTo(dto.getId())),
                    hasProperty("name", equalTo(dto.getName())),
                    hasProperty("email", equalTo(dto.getEmail()))
            )));
        }
    }

    @Test
    public void deleteUserWrongUser() throws Exception {
        Mockito.doThrow(NotFoundException.class)
                .when(userService).deleteUser(Mockito.anyLong());

        mvc.perform(delete("/admin/users/{id}", 1L))
                .andExpect(status().isNotFound());
    }


    @Test
    public void deleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(Mockito.anyLong());

        mvc.perform(delete("/admin/user/{id}", 1L))
                .andExpect(status().isNotFound());

    }

    private UserDto createUserDto(String name, String email, Long id) {
        UserDto user = new UserDto();
        user.setEmail(email);
        user.setName(name);
        user.setId(id);
        return user;
    }
}