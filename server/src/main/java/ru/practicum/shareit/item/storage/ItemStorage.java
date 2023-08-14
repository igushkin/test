package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemStorage {
    Item getItemById(Integer id);

    List<Item> getItemsByUserId(Integer userId);

    List<Item> findAvailableItemsByText(String text);

    Item createItem(Integer userId, Item item);

    Item patchItemName(Item item, Integer itemId, Integer userId);

    Item patchItemDescription(Item item, Integer itemId, Integer userId);

    Item patchItemStatus(Item item, Integer itemId, Integer userId);
}