package ru.practicum.main.repository.user;

import ru.practicum.main.model.User;

public interface UserRepositoryCustom {
    User getUserFromDb(Long id);
}
