package ru.practicum.main.mapper;

import ru.practicum.main.dto.NewUserRequestDto;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.dto.UserShortDto;
import ru.practicum.main.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(user.getName());
        return user;
    }

    public static User toUser(NewUserRequestDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        return userDto;
    }

    public static UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    public static List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(toUserDto(user));
        }
        return userDtos;
    }
}
