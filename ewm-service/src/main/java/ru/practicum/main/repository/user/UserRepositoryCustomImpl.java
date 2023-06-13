package ru.practicum.main.repository.user;

import org.springframework.context.annotation.Lazy;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.User;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final UserRepository userRepository;

    public UserRepositoryCustomImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserFromDb(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("User with id = %s not found", id))
        );
    }
}
