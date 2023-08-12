package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAllByUserId", userId);
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAll", userId);
        return itemRequestClient.getRequests(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "createItemRequest", userId, itemRequestDto);
        return itemRequestClient.createRequest(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByID(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @PathVariable Integer requestId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "createItemRequest", userId, requestId);
        return itemRequestClient.getRequest(userId, requestId);
    }
}