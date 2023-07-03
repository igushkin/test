package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConsistencyException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Integer, Item> itemStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public InMemoryItemStorage(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
        this.itemStorage = new HashMap<>();
    }

    @Override
    public Item getItemById(Integer id) {
        return itemStorage.get(id);
    }

    @Override
    public List<Item> getItemsByUserId(Integer userId) {
        return itemStorage.values()
                .stream()
                .filter(x -> x.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findAvailableItemsByText(String text) {
        return itemStorage.values()
                .stream()
                .filter(x -> x.getAvailable())
                .filter(x -> x.getName().toLowerCase().contains(text) || x.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(Integer userId, Item item) {
        var user = userStorage.getUserById(userId);

        if (user == null) {
            throw new NotFoundException("Пользователя с таким идентификатором не существует");
        }

        item.setId(ItemIdGenerator.getId());
        item.setOwner(user);
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item patchItemName(Item item, Integer itemId, Integer userId) {
        var originalItem = itemStorage.get(itemId);

        if (originalItem == null) {
            throw new NotFoundException("Итема с таким идентификатором не существует");
        }

        if (!originalItem.getOwner().getId().equals(userId)) {
            throw new ConsistencyException("Идентификатор владельца не совпадает");
        }

        originalItem.setName(item.getName());
        return originalItem;
    }

    @Override
    public Item patchItemDescription(Item item, Integer itemId, Integer userId) {
        var originalItem = itemStorage.get(itemId);

        if (originalItem == null) {
            throw new NotFoundException("Итема с таким идентификатором не существует");
        }

        if (!originalItem.getOwner().getId().equals(userId)) {
            throw new ConsistencyException("Идентификатор владельца не совпадает");
        }

        originalItem.setDescription(item.getDescription());
        return originalItem;
    }

    @Override
    public Item patchItemStatus(Item item, Integer itemId, Integer userId) {
        var originalItem = itemStorage.get(itemId);

        if (originalItem == null) {
            throw new NotFoundException("Итема с таким идентификатором не существует");
        }

        if (!originalItem.getOwner().getId().equals(userId)) {
            throw new ConsistencyException("Идентификатор владельца не совпадает");
        }

        originalItem.setAvailable(item.getAvailable());
        return originalItem;
    }

}