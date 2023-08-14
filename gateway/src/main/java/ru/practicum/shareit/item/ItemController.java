package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoValidator;

import javax.validation.Valid;
import java.security.InvalidParameterException;

@RestController
@RequestMapping(path = "/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping("{id}")
    public ResponseEntity<Object> getItemById(@PathVariable Integer id, @RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "getItemById", id, userId);
        return itemClient.getItem(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getItemById", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItems(@RequestParam(name = "text") String text) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "findItemByText", text);
        return itemClient.findItems(text);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemDto item) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "createItem", userId, item);
        var validationResult = ItemDtoValidator.validateCreation(item);
        for (var message : validationResult) {
            throw new InvalidParameterException(message);
        }
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemDto item, @PathVariable Integer itemId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}, {}", "patchItem", userId, item, itemId);
        var validationResult = ItemDtoValidator.validatePatch(item);
        for (var message : validationResult) {
            throw new InvalidParameterException(message);
        }

        return itemClient.patchItem(userId, item, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @Valid @RequestBody CommentDto comment, @PathVariable Integer itemId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}, {}", "addComment", userId, comment, itemId);
        return itemClient.addComment(userId, comment, itemId);
    }
}