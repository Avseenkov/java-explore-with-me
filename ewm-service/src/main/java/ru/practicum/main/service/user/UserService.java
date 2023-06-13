package ru.practicum.main.service.user;

import ru.practicum.main.dto.NewUserRequestDto;
import ru.practicum.main.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequestDto user);

    List<UserDto> findUsers(int from, int size, List<Long> ids);

    void deleteUser(Long id);
}
