package ru.practicum.main.service.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.NewUserRequestDto;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.mapper.UserMapper;
import ru.practicum.main.model.QUser;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(NewUserRequestDto user) {
        Optional<User> existUser = userRepository.findOne(
                QUser.user.name.eq(user.getName())
                        .or(QUser.user.email.eq(user.getEmail())));
        if (existUser.isPresent()) {
            throw new ConflictException("User is exist");
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findUsers(int from, int size, List<Long> ids) {

        int page = from / size;

        PageRequest pageRequest = PageRequest.of(page, size);
        Iterable<User> users = ids == null ? userRepository.findAll(pageRequest)
                : userRepository.findAll(QUser.user.id.in(ids), pageRequest);

        return UserMapper.mapToUserDto(users);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.getUserFromDb(id));
    }
}
