package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("{id}")
    public ItemDto getItemById(@PathVariable Integer id, @RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "getItemById", id, userId);
        return itemService.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getItemById", userId);
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByText(@RequestParam(name = "text") String text) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "findItemByText", text);
        return itemService.findItemByText(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemDto item) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "createItem", userId, item);
        return itemService.createItem(userId, item);
    }

    @PatchMapping("{id}")
    public ItemDto patchItem(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestBody ItemDto item, @PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}, {}", "patchItem", userId, item, id);
        return itemService.patchItem(item, id, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestBody Comment comment, @PathVariable Integer itemId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}, {}", "addComment", userId, comment, itemId);
        return itemService.createComment(userId, itemId, comment);
    }
}