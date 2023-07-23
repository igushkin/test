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
    /*
     * POST /requests — добавить новый запрос вещи.
     * Основная часть запроса — текст запроса, где пользователь описывает, какая именно вещь ему нужна.

     * GET /requests — получить список своих запросов вместе с данными об ответах на них.
     * Для каждого запроса должны указываться описание, дата и время создания и список ответов в формате:
     * id вещи, название, её описание description, а также requestId запроса и признак доступности вещи available.
     * Так в дальнейшем, используя указанные id вещей, можно будет получить подробную информацию о каждой вещи.
     * Запросы должны возвращаться в отсортированном порядке от более новых к более старым.

     * GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями.
     * С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
     * Запросы сортируются по дате создания: от более новых к более старым. Результаты должны возвращаться постранично.
     * Для этого нужно передать два параметра: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.

     * GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него
     * в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.
     * */

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