package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtendedDto;
import ru.practicum.shareit.exception.ExceptionMsg;
import ru.practicum.shareit.exception.NotFoundException;

import java.security.InvalidParameterException;
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

    @PostMapping()
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
    public List<BookingExtendedDto> getBookingsByOwnerId(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestParam(name = "state", required = false, defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getBookingsByOwnerId", userId, state);
        return bookingService.getBookingsByOwnerId(userId, state);
    }

    @GetMapping()
    public List<BookingExtendedDto> getAllBookingsByUserId(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @RequestParam(name = "state", required = false, defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}", "getAllBookingsByUserId", userId, state);
        return bookingService.getAllBookingsByUserId(userId, state);
    }

    @PatchMapping("{bookingId}")
    public BookingExtendedDto setBookingStatus(@RequestHeader(value = "X-Sharer-User-Id") Integer userId, @PathVariable Integer bookingId, @RequestParam(name = "approved") Boolean approved) {
        log.info("Получен запрос к методу: {}. Значение параметров: {}, {}, {}", "setBookingStatus", userId, bookingId, approved);
        var bookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        return bookingService.setBookingStatus(userId, bookingId, bookingStatus);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleException(Exception e) {
        var msg = new ExceptionMsg("Unknown state: UNSUPPORTED_STATUS");
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleException(NotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler({InvalidParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleException(RuntimeException e) {
        return e.getMessage();
    }
}