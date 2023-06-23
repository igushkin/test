package ru.practicum.shareit.user.storage;

public class UserIdGenerator {
    private static Integer id;

    static {
        id = 0;
    }

    private UserIdGenerator() {
    }

    public static Integer getId() {
        id += 1;
        return id;
    }
}