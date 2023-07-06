package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoValidator;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {

    private final ItemStorage itemStorage;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemStorage itemStorage, ItemRepository itemRepository) {
        this.itemStorage = itemStorage;
        this.itemRepository = itemRepository;
    }

    public ItemDto getItemById(Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getItemById", id);

        var item = itemRepository.findById(id);

        if (item.isEmpty()) {
            throw new NotFoundException("Item was not found");
        }

        return ItemMapper.toItemDto(item.get());
    }

    public List<ItemDto> getAllByUserId(Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAllByUserId", userId);

        return itemRepository.findByOwnerId(userId)
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

        return itemRepository
                .search(text)
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

        var item = itemRepository.save(ItemMapper.toItem(itemDto));

        return ItemMapper.toItemDto(item);
    }


    public ItemDto patchItem(ItemDto itemDto, Integer itemId, Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметров: {},{},{}", "patchItem", itemDto, itemId, userId);
        var validationResult = ItemDtoValidator.validatePatch(itemDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

        var item = ItemMapper.toItem(itemDto);
        Item itemFromDb = itemRepository.findById(itemId).get();

        if (item.getName() != null) {
            itemFromDb.setName(itemDto.getName());
            //itemFromDb = itemStorage.patchItemName(item, itemId, userId);
        }
        if (item.getDescription() != null) {
            itemFromDb.setDescription(itemDto.getDescription());
            //itemFromDb = itemStorage.patchItemDescription(item, itemId, userId);
        }
        if (item.getAvailable() != null) {
            itemFromDb.setAvailable(itemDto.getAvailable());
            //itemFromDb = itemStorage.patchItemStatus(item, itemId, userId);
        }

        return ItemMapper.toItemDto(itemFromDb);
    }
}