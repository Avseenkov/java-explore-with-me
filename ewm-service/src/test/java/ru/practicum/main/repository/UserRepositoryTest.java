package ru.practicum.main.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.NewUserRequestDto;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test-postgres")
@Transactional
class UserRepositoryTest {

    @Autowired
    public EntityManager em;

    @Autowired
    public UserRepository userRepository;

    User user;

    UserDto userDto;

    NewUserRequestDto newUser;

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

        em.createNativeQuery("INSERT INTO users (id, name, email) VALUES (?, ?,?)")
                .setParameter(1, 1L)
                .setParameter(2, user.getName())
                .setParameter(3, user.getEmail())
                .executeUpdate();

        TypedQuery<User> query = em.createQuery("SELECT U FROM User U WHERE U.email = :email", User.class);

        User userDb = query.setParameter("email", user.getEmail()).getSingleResult();

        assertThat(userDb.getName(), equalTo(user.getName()));
        assertThat(userDb.getEmail(), equalTo(user.getEmail()));

    }

    @Test
    void createWithWrongMinLengthEmail() {
        User wrongEmail = new User();
        wrongEmail.setName("John");
        wrongEmail.setEmail("kk");

        PersistenceException exception = Assertions.assertThrows(PersistenceException.class, () -> {

            em.createNativeQuery("INSERT INTO users (id, name, email) VALUES (?, ?,?)")
                    .setParameter(1, 1L)
                    .setParameter(2, wrongEmail.getName())
                    .setParameter(3, wrongEmail.getEmail())
                    .executeUpdate();
        });

        assertTrue(exception.getCause() instanceof ConstraintViolationException);

    }
}