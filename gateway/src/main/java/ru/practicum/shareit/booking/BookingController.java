package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "createBooking", userId, bookingDto);
        return bookingClient.bookItem(userId, bookingDto);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @PathVariable Integer bookingId) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getBookingById", userId, bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwnerId(
            @RequestHeader(value = "X-Sharer-User-Id") Integer userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getBookingsByOwnerId", userId, stateParam);
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getBookingsByOwnerId(userId, state, from, size);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> setBookingStatus(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @PathVariable Integer bookingId, @RequestParam(name = "approved") Boolean status) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}, {}", "setBookingStatus", userId, bookingId, status);
        //var bookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        return bookingClient.setBookingStatus(userId, bookingId, status);
    }
}