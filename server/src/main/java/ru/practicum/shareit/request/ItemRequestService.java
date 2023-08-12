package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ItemRequestDto createItemRequest(Integer userId, ItemRequestDto itemRequestDto) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "createItemRequest", userId, itemRequestDto);

        User user;

        try {
            user = userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new NotFoundException("User was not found");
        }

        var itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllByUserId(Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAllByUserId", userId);
        User user;

        try {
            user = userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new NotFoundException("User was not found");
        }

        var requests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId)
                .stream()
                .map(x -> ItemRequestMapper.toItemRequestDto(x))
                .collect(Collectors.toList());

        var groupedItemsByRequest = itemRepository.findAllByRequestIdIn(requests.stream().map(x -> x.getId()).collect(Collectors.toList()))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.groupingBy(item -> item.getRequestId()));

        for (var request : requests) {
            if (groupedItemsByRequest.containsKey(request.getId()))
                request.setItems(groupedItemsByRequest.get(request.getId()));
        }

        return requests;
    }

    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAll(Integer userId, Integer from, Integer size) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAll", userId);
        User user;

        try {
            user = userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new NotFoundException("User was not found");
        }

        if (from == null || size == null) {
            return itemRequestRepository.findAllByRequestorIdIsNot(userId)
                    .stream()
                    .map(x -> ItemRequestMapper.toItemRequestDto(x))
                    .collect(Collectors.toList());
        } else {
            var page = (from / size);

            var pageRequest = PageRequest.of(page, size, Sort.by("created").descending());
            var requests = itemRequestRepository.findAllByRequestorIdIsNot(userId, pageRequest)
                    .getContent()
                    .stream()
                    .map(ItemRequestMapper::toItemRequestDto)
                    .collect(Collectors.toList());

            var groupedItemsByRequest = itemRepository.findAllByRequestIdIn(requests.stream().map(x -> x.getId()).collect(Collectors.toList()))
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.groupingBy(item -> item.getRequestId()));


            for (var request : requests) {
                if (groupedItemsByRequest.containsKey(request.getId())) {
                    request.setItems(groupedItemsByRequest.get(request.getId()));
                }
            }

            return requests;
        }
    }

    @Transactional(readOnly = true)
    public ItemRequestDto getById(Integer requestId, Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "getById", requestId, userId);
        ItemRequest request;

        try {
            request = itemRequestRepository.findById(requestId).get();
        } catch (Exception e) {
            throw new NotFoundException("Request was not found");
        }

        try {
            userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new NotFoundException("User was not found");
        }

        var groupedItemsByRequest = itemRepository.findAllByRequestIdIn(List.of(request.getId()))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.groupingBy(item -> item.getRequestId()));

        var requestDto = ItemRequestMapper.toItemRequestDto(request);
        requestDto.setItems(groupedItemsByRequest.get(request.getId()));
        return requestDto;
    }
}