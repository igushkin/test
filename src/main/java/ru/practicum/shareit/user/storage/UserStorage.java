package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User getUserById(Integer id);

    User createUser(User user);

    User patchUserName(User user, Integer userId);

    User patchUserEmail(User user, Integer userId);

    void deleteUser(Integer id);
}