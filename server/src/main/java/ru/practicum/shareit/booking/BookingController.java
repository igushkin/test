package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtendedDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingExtendedDto createBooking(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "createBooking", userId, bookingDto);
        return bookingService.createBooking(userId, bookingDto);
    }

    @GetMapping("{bookingId}")
    public BookingExtendedDto getBookingById(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @PathVariable Integer bookingId) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getBookingById", userId, bookingId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingExtendedDto> getBookingsByOwnerId(
            @RequestHeader(value = "X-Sharer-User-Id") Integer userId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") BookingState state,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getBookingsByOwnerId", userId, state);
        return bookingService.getBookingsByOwnerId(userId, state, from, size);
    }

    @GetMapping
    public List<BookingExtendedDto> getAllBookingsByUserId(
            @RequestHeader(value = "X-Sharer-User-Id") Integer userId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") BookingState state,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getAllBookingsByUserId", userId, state);
        return bookingService.getAllBookingsByUserId(userId, state, from, size);
    }

    @PatchMapping("{bookingId}")
    public BookingExtendedDto setBookingStatus(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @PathVariable Integer bookingId, @RequestParam(name = "approved") Boolean approved) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}, {}", "setBookingStatus", userId, bookingId, approved);
        var bookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        return bookingService.setBookingStatus(userId, bookingId, bookingStatus);
    }
}