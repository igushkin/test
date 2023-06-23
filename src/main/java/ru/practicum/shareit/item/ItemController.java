package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ConsistencyException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.security.InvalidParameterException;
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
    public ItemDto getItemById(@PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getItemById", id);
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAllByUserId", userId);
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByText(@RequestParam(name = "text") String text) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "findItemByText", text);
        return itemService.findItemByText(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemDto item) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "createItem", item);
        return itemService.createItem(userId, item);
    }

    @PatchMapping("{id}")
    public ItemDto patchItem(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestBody ItemDto item, @PathVariable Integer id) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "patchItem", id);
        return itemService.patchItem(item, id, userId);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({InvalidParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleException(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler({ConsistencyException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public String handleException(ConsistencyException e) {
        return e.getMessage();
    }
}