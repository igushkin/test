package ru.practicum.shareit.item;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ConsistencyException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@NoArgsConstructor
public class ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemService(
            ItemRepository itemRepository,
            UserRepository userRepository,
            BookingRepository bookingRepository,
            CommentRepository commentRepository,
            ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    public ItemDto getItemById(Integer itemId, Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}, {}", "getItemById", itemId, userId);

        var item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            throw new NotFoundException("Item was not found");
        }

        BookingDto lastBooking = null;
        BookingDto nextBooking = null;

        if (item.get().getOwner().getId().equals(userId)) {
            var queryResult = bookingRepository.findBookingsBeforeDateAndByItemId(itemId, LocalDateTime.now(), PageRequest.of(0, 1));
            if (queryResult.size() > 0) {
                lastBooking = BookingMapper.toBookingDto(queryResult.get(0));
            }
            queryResult = bookingRepository.findBookingsAfterDateAndByItemId(itemId, LocalDateTime.now(), PageRequest.of(0, 1));
            if (queryResult.size() > 0) {
                nextBooking = BookingMapper.toBookingDto(queryResult.get(0));
            }
        }

        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(x -> CommentMapper.toCommentDto(x))
                .collect(Collectors.toList());

        return ItemMapper.toItemDtoWithBookings(item.get(), lastBooking, nextBooking, comments);
    }

    public List<ItemDto> getAllByUserId(Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметра: {}", "getAllByUserId", userId);

        var items = itemRepository.findByOwnerIdOrderById(userId)
                .stream()
                .map(x -> ItemMapper.toItemDto(x))
                .collect(Collectors.toList());

        var allBookings = bookingRepository.findAllBookingsByItemIds(items.stream().map(x -> x.getId()).collect(Collectors.toList()))
                .stream()
                .map(x -> BookingMapper.toBookingDto(x))
                .collect(Collectors.toList());

        var lastBookings = allBookings
                .stream()
                .filter(x -> x.getStart().isBefore(LocalDateTime.now()))
                .collect(Collectors.groupingBy(x -> x.getItemId()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().stream().max(Comparator.comparing(BookingDto::getStart))));

        var nextBookings = allBookings
                .stream()
                .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                .collect(Collectors.groupingBy(x -> x.getItemId()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().stream().min(Comparator.comparing(BookingDto::getStart))));

        var comments = commentRepository.findByItemIdIn(items.stream().map(x -> x.getId()).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.groupingBy(x -> x.getItem().getId()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().stream().map(v -> CommentMapper.toCommentDto(v)).collect(Collectors.toList())));

        for (ItemDto itemDto : items) {
            var lastBooking = lastBookings.containsKey(itemDto.getId()) ? lastBookings.get(itemDto.getId()).orElse(null) : null;
            var nextBooking = nextBookings.containsKey(itemDto.getId()) ? nextBookings.get(itemDto.getId()).orElse(null) : null;
            List<CommentDto> itemComments = comments.containsKey(itemDto.getId()) ? comments.get(itemDto.getId()) : List.of();

            itemDto.setLastBooking(lastBooking);
            itemDto.setNextBooking(nextBooking);
            itemDto.setComments(itemComments);
        }

        return items;
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

    @Transactional
    public ItemDto createItem(Integer userId, ItemDto itemDto) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "createItem", userId, itemDto);
        var validationResult = ItemDtoValidator.validateCreation(itemDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

        User owner = null;

        try {
            owner = userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new NotFoundException("Пользователь не найден");
        }

        var item = ItemMapper.toItem(itemDto);
        ItemRequest request = null;

        if (itemDto.getRequestId() != null) {
            try {
                request = itemRequestRepository.findById(itemDto.getRequestId()).get();
            } catch (Exception e) {
                throw new NotFoundException("ItemRequest was not found");
            }
        }

        item.setOwner(owner);
        item.setRequest(request);

        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    public ItemDto patchItem(ItemDto itemDto, Integer itemId, Integer userId) {
        log.info("Получен запрос к методу: {}. Значение параметров: {},{},{}", "patchItem", itemDto, itemId, userId);
        var validationResult = ItemDtoValidator.validatePatch(itemDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

        Item itemFromDb = itemRepository.findById(itemId).get();

        if (!itemFromDb.getOwner().getId().equals(userId)) {
            throw new ConsistencyException("Обновление доступно только владельщу вещи");
        }

        if (itemDto.getName() != null) {
            itemFromDb.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemFromDb.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemFromDb.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepository.save(itemFromDb));
    }

    @Transactional
    public CommentDto createComment(Integer userId, Integer itemId, Comment comment) {
        log.info("Получен запрос к методу: {}. Значение параметров: {},{},{}", "createComment", userId, itemId, comment);

        var item = itemRepository.findById(itemId);
        var user = userRepository.findById(userId);

        if (item.isEmpty() || user.isEmpty()) {
            throw new NotFoundException("Переданые неверые идентификаторы");
        }

        if (Strings.isBlank(comment.getText())) {
            throw new InvalidParameterException("");
        }

        if (bookingRepository.findByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now()).size() == 0) {
            throw new InvalidParameterException("");
        }

        comment.setItem(item.get());
        comment.setAuthor(user.get());
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}