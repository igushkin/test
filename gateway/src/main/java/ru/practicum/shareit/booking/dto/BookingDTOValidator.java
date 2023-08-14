package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

@UtilityClass
public class BookingDTOValidator {
    public static Optional<String> validateCreation(BookingDto bookingDto) {
        if (bookingDto.getStart() == null) {
            return Optional.of("Дата обязательное поле");
        }
        if (bookingDto.getEnd() == null) {
            return Optional.of("Дата обязательное поле");
        }
        if (bookingDto.getEnd().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            return Optional.of("Дата окончания не может быть в прошлом");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            return Optional.of("Дата окончания должна быть позже даты начала");
        }
        if (bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            return Optional.of("Дата окончания должна быть позже даты начала");
        }
        if (bookingDto.getStart().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            return Optional.of("Дата начала должна быть позже текущей даты");
        }
        return Optional.empty();
    }
}