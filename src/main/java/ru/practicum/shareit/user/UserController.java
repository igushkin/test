package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Получен запрос к методу: {}", "getUsers");
        return userService.getUsers();
    }

    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getUserById", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "createUser", user);
        return userService.createUser(user);
    }

    @PatchMapping("{id}")
    public UserDto patchUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "patchUser", userDto, id);
        return userService.patchUser(userDto, id);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "deleteUser", id);
        userService.deleteUser(id);
    }
}