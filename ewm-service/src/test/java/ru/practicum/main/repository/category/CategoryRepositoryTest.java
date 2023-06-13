package ru.practicum.main.repository.category;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test-postgres")
@Transactional
class CategoryRepositoryTest {

    @Autowired
    public EntityManager em;

    @Autowired
    public CategoryRepository categoryRepository;


    @Test
    public void validateMinLength() {
        PersistenceException exception = Assertions.assertThrows(PersistenceException.class, () -> {
            em.createNativeQuery("INSERT INTO categories (id, name) VALUES (?, ?)")
                    .setParameter(1, 1L)
                    .setParameter(2, "a")
                    .executeUpdate();
        });

        assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }


}