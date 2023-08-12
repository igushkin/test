package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDTOValidator;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtendedDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public BookingExtendedDto createBooking(Integer userId, BookingDto bookingDto) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "createBooking", userId, bookingDto);

        User user = null;
        Item item = null;

        try {
            user = userRepository.findById(userId).get();
            item = itemRepository.findById(bookingDto.getItemId()).get();
        } catch (Exception e) {
            throw new NotFoundException("Не найдены связанные сущности." + e.getMessage());
        }

        var validationResult = BookingDTOValidator.validateCreation(bookingDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

        if (!item.getAvailable()) {
            throw new InvalidParameterException("");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("");
        }

        var booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);

        return BookingMapper.toExtendedBookingDto(booking);
    }

    public List<BookingExtendedDto> getBookingsByOwnerId(Integer userId, BookingState bookingState, Integer from, Integer size) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getBookingsByOwnerId", userId, bookingState);

        if (bookingRepository.findOwnerById(userId).size() == 0) {
            throw new NotFoundException("Владелец вещи не найден");
        }

        if (from == null && size == null) {
            from = 0;
            size = Integer.MAX_VALUE;
        }

        var pageRequest = getPage(from, size);

        List<Booking> bookings;

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllBookingsByOwnerId(userId, null, pageRequest);
                break;
            case WAITING:
            case REJECTED:
                var bookingStatus = bookingState.equals(BookingState.WAITING) ? BookingStatus.WAITING.toString() : BookingStatus.REJECTED.toString();
                bookings = bookingRepository.findAllBookingsByOwnerId(userId, bookingStatus, pageRequest);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllCurrentBookingsByOwnerIdWithFetch(userId, LocalDateTime.now(), pageRequest);
                break;
            case PAST:
                bookings = bookingRepository.findAllPastBookingsByOwnerIdWithFetch(userId, LocalDateTime.now(), pageRequest);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllFutureBookingsByOwnerIdWithFetch(userId, LocalDateTime.now(), pageRequest);
                break;
            default:
                throw new NotFoundException("");
        }

        return bookings.stream()
                .map(x -> BookingMapper.toExtendedBookingDto(x))
                .collect(Collectors.toList());
    }

    public BookingExtendedDto getBookingById(Integer userId, Integer bookingId) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getBookingById", userId, bookingId);

        Booking booking = null;
        User user = null;

        try {
            booking = bookingRepository.findById(bookingId).get();
            user = userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new NotFoundException("");
        }

        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Пользователь не является ни владельцем ни арендователем");
        }

        return BookingMapper.toExtendedBookingDto(booking);
    }

    public List<BookingExtendedDto> getAllBookingsByUserId(Integer userId, BookingState bookingState, Integer from, Integer size) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getAllBookingsByUserId", userId, bookingState);

        try {
            userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new NotFoundException("Пользователь не найден");
        }

        List<Booking> bookings;

        if (from == null && size == null) {
            from = 0;
            size = Integer.MAX_VALUE;
        }

        PageRequest pageRequest = getPage(from, size);

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllBookingsByUserIdAndStatusWithFetch(userId, null, pageRequest);
                break;
            case WAITING:
            case REJECTED:
                var bookingStatus = bookingState.equals(BookingState.WAITING) ? BookingStatus.WAITING.toString() : BookingStatus.REJECTED.toString();
                bookings = bookingRepository.findAllBookingsByUserIdAndStatusWithFetch(userId, bookingStatus, pageRequest);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllCurrentBookingsByUserIdWithFetch(userId, LocalDateTime.now(), pageRequest);
                break;
            case PAST:
                bookings = bookingRepository.findAllPastBookingsByUserIdWithFetch(userId, LocalDateTime.now(), pageRequest);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllFutureBookingsByUserIdWithFetch(userId, LocalDateTime.now(), pageRequest);
                break;
            default:
                throw new NotFoundException("");
        }

        return bookings.stream()
                .map(x -> BookingMapper.toExtendedBookingDto(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingExtendedDto setBookingStatus(Integer userId, Integer bookingId, BookingStatus bookingStatus) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}, {}", "setBookingStatus", userId, bookingId, bookingStatus);

        var booking = bookingRepository.findById(bookingId).get();

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new InvalidParameterException("Статус бронирования не может быть изменен");
        }

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Статус может быть изменен только владельцем вещи");
        }

        booking.setStatus(bookingStatus);
        booking = bookingRepository.save(booking);
        return BookingMapper.toExtendedBookingDto(booking);
    }

    private PageRequest getPage(Integer from, Integer size) {
        if (from < 0 || size < 1) {
            throw new RuntimeException("Not valid page arams");
        }

        var pageIndex = from / size;

        return PageRequest.of(pageIndex, size);
    }
}