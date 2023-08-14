package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @GetMapping
    public List<ItemRequestDto> getAllByUserId(@RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAllByUserId", userId);
        return itemRequestService.getAllByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestParam(required = false) Integer from, @RequestParam(required = false) Integer size) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAll", userId);
        return itemRequestService.getAll(userId, from, size);
    }

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "createItemRequest", userId, itemRequestDto);
        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getByID(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @PathVariable Integer requestId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "createItemRequest", userId, requestId);
        return itemRequestService.getById(requestId, userId);
    }
}