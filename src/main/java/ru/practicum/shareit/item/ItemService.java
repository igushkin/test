package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoValidator;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {

    private final ItemStorage itemStorage;

    public ItemService(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    public ItemDto getItemById(Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getItemById", id);
        return ItemMapper.toItemDto(itemStorage.getItemById(id));
    }

    public List<ItemDto> getAllByUserId(Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAllByUserId", userId);
        return itemStorage.getItemsByUserId(userId)
                .stream()
                .map(x -> ItemMapper.toItemDto(x))
                .collect(Collectors.toList());
    }

    public List<ItemDto> findItemByText(String text) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "findItemByText", text);
        text = text.trim().toLowerCase();

        if (Strings.isBlank(text)) {
            return List.of();
        }

        return itemStorage.findAvailableItemsByText(text)
                .stream()
                .map(x -> ItemMapper.toItemDto(x))
                .collect(Collectors.toList());
    }

    public ItemDto createItem(Integer userId, ItemDto itemDto) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "createItem", itemDto);
        var validationResult = ItemDtoValidator.validateCreation(itemDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

        return ItemMapper.toItemDto(itemStorage.createItem(userId, ItemMapper.toItem(itemDto)));
    }


    public ItemDto patchItem(ItemDto itemDto, Integer itemId, Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметров: {},{},{}", "patchItem", itemDto, itemId, userId);
        var validationResult = ItemDtoValidator.validatePatch(itemDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

        var item = ItemMapper.toItem(itemDto);
        Item result = null;

        if (item.getName() != null) {
            result = itemStorage.patchItemName(item, itemId, userId);
        }
        if (item.getDescription() != null) {
            result = itemStorage.patchItemDescription(item, itemId, userId);
        }
        if (item.getAvailable() != null) {
            result = itemStorage.patchItemStatus(item, itemId, userId);
        }

        return ItemMapper.toItemDto(result);

    }
}