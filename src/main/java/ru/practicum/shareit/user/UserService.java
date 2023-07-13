package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateKeyException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoValidator;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserStorage userStorage, UserRepository userRepository) {
        this.userStorage = userStorage;
        this.userRepository = userRepository;
    }

    public List<UserDto> getUsers() {
        log.info("Получен запрос к методу: {}.", "getUsers");
        return userRepository.findAll()
                .stream()
                .map(x -> UserMapper.toUserDto(x))
                .collect(Collectors.toList());
    }

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
        var validationResult = UserDtoValidator.validateCreation(userDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

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
        var validationResult = UserDtoValidator.validatePatch(userDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

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