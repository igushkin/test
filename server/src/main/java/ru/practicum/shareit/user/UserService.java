package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateKeyException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        log.info("Получен запрос к методу: {}.", "getUsers");
        return userRepository.findAll()
                .stream()
                .map(x -> UserMapper.toUserDto(x))
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public UserDto getUserById(Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}.", "getUserById", id);

        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new NotFoundException("User was not found");
        }

        return UserMapper.toUserDto(user.get());
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}.", "createUser", userDto);

        User user;

        try {
            user = userRepository.save(UserMapper.toUser(userDto));
        } catch (Exception e) {
            throw new DuplicateKeyException("Пользователь с таким email уже существует");
        }

        return UserMapper.toUserDto(user);
    }

    @Transactional
    public UserDto patchUser(UserDto userDto, Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}.", "patchUser", userDto, userId);

        User userFromDB = userRepository.findById(userId).get();

        if (userDto.getName() != null) {
            userFromDB.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userFromDB.setEmail(userDto.getEmail());
        }

        try {
            userRepository.saveAndFlush(userFromDB);
        } catch (Exception e) {
            throw new DuplicateKeyException("Пользователь с таким email уже существует");
        }

        return UserMapper.toUserDto(userFromDB);
    }

    @Transactional
    public void deleteUser(Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}.", "deleteUser", id);
        userRepository.deleteById(id);
    }
}