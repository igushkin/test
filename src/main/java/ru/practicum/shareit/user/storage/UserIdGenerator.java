package ru.practicum.shareit.user.storage;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserIdGenerator {
    private static Integer id;

    static {
        id = 0;
    }

    public static Integer getId() {
        id += 1;
        return id;
    }
}