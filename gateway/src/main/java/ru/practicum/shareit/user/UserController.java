package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoValidator;

import javax.validation.Valid;
import java.security.InvalidParameterException;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Получен запрос к методу: {}", "getUsers");
        return userClient.getUsers();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getUserById", id);
        return userClient.getUser(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto user) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "createUser", user);
        var validationResult = UserDtoValidator.validateCreation(user);
        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }
        return userClient.createUser(user);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> patchUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "patchUser", userDto, id);
        var validationResult = UserDtoValidator.validatePatch(userDto);
        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }
        return userClient.patchUser(userDto, id);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "deleteUser", id);
        userClient.deleteUser(id);
    }
}