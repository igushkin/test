package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

/*    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }*/

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
        return userClient.createUser(user);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> patchUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "patchUser", userDto, id);
        return userClient.patchUser(userDto, id);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "deleteUser", id);
        userClient.deleteUser(id);
    }
}