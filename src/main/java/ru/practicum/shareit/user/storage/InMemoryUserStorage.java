package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConsistencyException;
import ru.practicum.shareit.exception.DuplicateKeyException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userStorage;
    private final Set<String> emailStorage;

    public InMemoryUserStorage() {
        this.userStorage = new HashMap<>();
        this.emailStorage = new HashSet<>();
    }

    @Override
    public List<User> getUsers() {
        return userStorage.values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Integer id) {
        return userStorage.get(id);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userStorage.containsKey(id)) {
            throw new NotFoundException("Такого пользователя не сущ.");
        }

        emailStorage.remove(userStorage.get(id).getEmail());
        userStorage.remove(id);
    }

    @Override
    public User createUser(User user) {
        if (emailStorage.contains(user.getEmail())) {
            throw new DuplicateKeyException("Пользователь с таким email уже сущ.");
        }
        user.setId(UserIdGenerator.getId());
        userStorage.put(user.getId(), user);
        emailStorage.add(user.getEmail());

        return user;
    }

    @Override
    public User patchUserName(User user, Integer id) {
        var originalUser = userStorage.get(id);

        if (originalUser == null) {
            throw new NotFoundException("Пользователя с таким идентификатором не существует");
        }

        originalUser.setName(user.getName());
        return originalUser;
    }

    @Override
    public User patchUserEmail(User user, Integer userId) {
        var originalUser = userStorage.get(userId);

        if (originalUser == null) {
            throw new NotFoundException("Пользователя с таким идентификатором не существует");
        }
        if (emailStorage.contains(user.getEmail()) && !originalUser.getEmail().equals(user.getEmail())) {
            throw new ConsistencyException("Пользователя с таким идентификатором не существует");
        }

        emailStorage.remove(originalUser.getEmail());
        emailStorage.add(user.getEmail());

        originalUser.setEmail(user.getEmail());
        return originalUser;
    }
}