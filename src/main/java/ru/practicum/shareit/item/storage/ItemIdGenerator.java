package ru.practicum.shareit.item.storage;

public class ItemIdGenerator {
    private static Integer id;

    static {
        id = 0;
    }

    public static Integer getId() {
        id += 1;
        return id;
    }
}