package ru.practicum.main.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.NewUserRequestDto;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.UserMapper;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.user.UserRepository;
import ru.practicum.main.service.user.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceImplTestIntegration {

    @Autowired
    public UserServiceImpl userService;

    @Autowired
    public EntityManager em;

    User user;

    UserDto userDto;

    NewUserRequestDto newUser;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@test.com1");
        user.setName("test_test");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setName("test_test");

        newUser = new NewUserRequestDto();
        newUser.setEmail("test@test.com");
        newUser.setName("test_test");
    }

    @Test
    void createUser() {
        UserDto userFromDb = userService.createUser(newUser);

        assertThat(userFromDb.getName(), equalTo(newUser.getName()));
        assertThat(userFromDb.getEmail(), equalTo(newUser.getEmail()));

        TypedQuery<User> query = em.createQuery("SELECT U FROM User U WHERE U.email = :email", User.class);
        User userDb = query.setParameter("email", newUser.getEmail()).getSingleResult();

        assertThat(userDb.getName(), equalTo(newUser.getName()));
        assertThat(userDb.getEmail(), equalTo(newUser.getEmail()));
    }

    @Test
    void getUsers() {
        NewUserRequestDto user1 = createUser("test1", "test@test.ru");
        NewUserRequestDto user2 = createUser("test2", "test2@test.ru");
        NewUserRequestDto user3 = createUser("test3", "test3@test.ru");

        userRepository.save(UserMapper.toUser(user1));
        userRepository.save(UserMapper.toUser(user2));
        userRepository.save(UserMapper.toUser(user3));

        List<UserDto> users = userService.findUsers(0, 10, null);
        assertThat(users.size(), equalTo(3));

        List<NewUserRequestDto> initUsers = List.of(user1, user2, user3);

        for (NewUserRequestDto initUser : initUsers) {
            assertThat(users, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(initUser.getName())),
                    hasProperty("email", equalTo(initUser.getEmail()))
            )));
        }
    }

    @Test
    public void getFirst2Users() {
        NewUserRequestDto user1 = createUser("test1", "test@test.ru");
        NewUserRequestDto user2 = createUser("test2", "test2@test.ru");
        NewUserRequestDto user3 = createUser("test3", "test3@test.ru");

        userRepository.save(UserMapper.toUser(user1));
        userRepository.save(UserMapper.toUser(user2));
        userRepository.save(UserMapper.toUser(user3));


        List<UserDto> users = userService.findUsers(0, 2, null);
        assertThat(users.size(), equalTo(2));

    }

    @Test
    public void getSecondPage() {
        NewUserRequestDto user1 = createUser("test1", "test@test.ru");
        NewUserRequestDto user2 = createUser("test2", "test2@test.ru");
        NewUserRequestDto user3 = createUser("test3", "test3@test.ru");

        userRepository.save(UserMapper.toUser(user1));
        userRepository.save(UserMapper.toUser(user2));
        userRepository.save(UserMapper.toUser(user3));


        List<UserDto> users = userService.findUsers(3, 2, null);
        assertThat(users.size(), equalTo(1));

    }

    @Test
    public void getEmptyList() {


        List<UserDto> users = userService.findUsers(0, 10, null);
        assertThat(users.size(), equalTo(0));


    }

    @Test
    public void getUsersByIds() {

        List<User> users = List.of(
                createUserForDb("test", "test1@test.com"),
                createUserForDb("tes2", "test2@test.com"),
                createUserForDb("test3", "test3@test.com")
        );

        for (User user1 : users) {
            em.persist(user1);
        }

        em.flush();

        List<Long> userForTest = List.of(
                users.get(0).getId(),
                users.get(1).getId()
        );

        List<UserDto> usersFromDb = userService.findUsers(0, 10, userForTest);
        assertThat(usersFromDb.size(), equalTo(2));

        for (Long userId : userForTest) {
            assertThat(usersFromDb, hasItem(anyOf(
                    hasProperty("id", equalTo(userId))
            )));
        }

    }

    @Test
    public void deleteUser() {
        List<User> users = List.of(
                createUserForDb("test", "test1@test.com"),
                createUserForDb("tes2", "test2@test.com"),
                createUserForDb("test3", "test3@test.com")
        );

        for (User user1 : users) {
            em.persist(user1);
        }

        em.flush();

        List<UserDto> usersFromDb = userService.findUsers(0, 10, null);
        assertThat(usersFromDb.size(), equalTo(3));

        userService.deleteUser(users.get(0).getId());

        usersFromDb = userService.findUsers(0, 10, null);
        assertThat(usersFromDb.size(), equalTo(2));


    }

    @Test
    public void deleteWrongUser() {

        Assertions.assertThrows(NotFoundException.class, () ->
                userService.deleteUser(1L));
    }

    private NewUserRequestDto createUser(String name, String email) {
        NewUserRequestDto user = new NewUserRequestDto();
        user.setEmail(email);
        user.setName(name);
        return user;
    }

    private User createUserForDb(String name, String email) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}