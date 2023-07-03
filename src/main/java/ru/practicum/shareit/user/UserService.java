package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoValidator;
import ru.practicum.shareit.user.storage.UserStorage;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> getUsers() {
        log.info("Получен запрос к методу: {}.", "getUsers");
        return userStorage.getUsers()
                .stream()
                .map(x -> UserMapper.toUserDto(x))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}.", "getUserById", id);
        return UserMapper.toUserDto(userStorage.getUserById(id));
    }

    public UserDto createUser(UserDto userDto) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}.", "createUser", userDto);
        var validationResult = UserDtoValidator.validateCreation(userDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

        return UserMapper.toUserDto(userStorage.createUser(UserMapper.toUser(userDto)));
    }

    public UserDto patchUser(UserDto userDto, Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}.", "patchUser", userDto, userId);
        var validationResult = UserDtoValidator.validatePatch(userDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

        User result = null;

        if (userDto.getName() != null) {
            result = userStorage.patchUserName(UserMapper.toUser(userDto), userId);
        }
        if (userDto.getEmail() != null) {
            result = userStorage.patchUserEmail(UserMapper.toUser(userDto), userId);
        }

        return UserMapper.toUserDto(result);
    }

    public void deleteUser(Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}.", "deleteUser", id);
        userStorage.deleteUser(id);
    }
}