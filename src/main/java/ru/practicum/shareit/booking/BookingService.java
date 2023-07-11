package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDTOValidator;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtendedDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    public List<BookingExtendedDto> te() {
        return bookingRepository.findAllFutureBookingsByUserIdWithFetch(1, LocalDateTime.now())
                .stream()
                .map(x -> BookingMapper.toExtendedBookingDto(x))
                .collect(Collectors.toList());
    }

    public BookingExtendedDto createBooking(Integer userId, BookingDto bookingDto) {
        var user = userRepository.findById(userId);
        var item = itemRepository.findById(bookingDto.getItemId());

        var validationResult = BookingDTOValidator.validateCreation(bookingDto);

        if (validationResult.size() > 0) {
            throw new InvalidParameterException(validationResult.get(0));
        }

        if (user.isEmpty()) {
            throw new NotFoundException("");
        }
        if (item.isEmpty()) {
            throw new NotFoundException("");
        }
        if (!item.get().getAvailable()) {
            throw new InvalidParameterException("");
        }
        if (bookingDto.getEnd().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            throw new InvalidParameterException();
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new InvalidParameterException();
        }
        if (bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new InvalidParameterException();
        }
        if (bookingDto.getStart().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            throw new InvalidParameterException();
        }
        if (item.get().getOwner().getId().equals(userId)) {
            throw new NotFoundException("");
        }


        var booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user.get());
        booking.setItem(item.get());
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);

        return BookingMapper.toExtendedBookingDto(booking);
    }

    public List<BookingExtendedDto> getBookingsByOwnerId(Integer userId, BookingState bookingState) {

        if (bookingRepository.findOwnerById(userId).size() == 0)
            throw new NotFoundException("");

        List<Booking> bookings;

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllBookingsByOwnerId(userId, null);
                break;
            case WAITING:
            case REJECTED:
                var bookingStatus = bookingState.equals(BookingState.WAITING) ? BookingStatus.WAITING.toString() : BookingStatus.REJECTED.toString();
                bookings = bookingRepository.findAllBookingsByOwnerId(userId, bookingStatus);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllCurrentBookingsByOwnerIdWithFetch(userId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllPastBookingsByOwnerIdWithFetch(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllFutureBookingsByOwnerIdWithFetch(userId, LocalDateTime.now());
                break;
            default:
                throw new NotFoundException("");
        }

        return bookings.stream()
                .map(x -> BookingMapper.toExtendedBookingDto(x))
                .collect(Collectors.toList());
    }

    public BookingExtendedDto getBookingById(Integer userId, Integer bookingId) {
        Booking booking = null;
        User user = null;

        try {
            booking = bookingRepository.findById(bookingId).get();
            user = userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new NotFoundException("");
        }

        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("");
        }

        return BookingMapper.toExtendedBookingDto(booking);
    }

    public List<BookingExtendedDto> getAllBookingsByUserId(Integer userId, BookingState bookingState) {

        try {
            userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new NotFoundException("");
        }
        switch (bookingState) {
            case ALL:
                return bookingRepository.findAllBookingsByUserIdAndStatusWithFetch(userId, null)
                        .stream()
                        .map(x -> BookingMapper.toExtendedBookingDto(x))
                        .collect(Collectors.toList());
            case WAITING:
            case REJECTED:
                var bookingStatus = bookingState.equals(BookingState.WAITING) ? BookingStatus.WAITING.toString() : BookingStatus.REJECTED.toString();
                return bookingRepository.findAllBookingsByUserIdAndStatusWithFetch(userId, bookingStatus)
                        .stream()
                        .map(x -> BookingMapper.toExtendedBookingDto(x))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllCurrentBookingsByUserIdWithFetch(userId, LocalDateTime.now())
                        .stream()
                        .map(x -> BookingMapper.toExtendedBookingDto(x))
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllPastBookingsByUserIdWithFetch(userId, LocalDateTime.now())
                        .stream()
                        .map(x -> BookingMapper.toExtendedBookingDto(x))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllFutureBookingsByUserIdWithFetch(userId, LocalDateTime.now())
                        .stream()
                        .map(x -> BookingMapper.toExtendedBookingDto(x))
                        .collect(Collectors.toList());
            default:
                throw new NotFoundException("");
        }
    }

    public BookingExtendedDto setBookingStatus(Integer userId, Integer bookingId, BookingStatus bookingStatus) {
        var booking = bookingRepository.findById(bookingId).get();

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new InvalidParameterException("");
        }

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("");
        }

        booking.setStatus(bookingStatus);
        booking = bookingRepository.save(booking);
        return BookingMapper.toExtendedBookingDto(booking);
    }
}