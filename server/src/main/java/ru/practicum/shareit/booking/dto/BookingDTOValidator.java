package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BookingDTOValidator {
    public static List<String> validateCreation(BookingDto bookingDto) {
        List<String> errMessages = new ArrayList<>();

        if (bookingDto.getStart() == null) {
            var errMsg = "Дата обязательное поле";
            errMessages.add(errMsg);
        }
        if (bookingDto.getEnd() == null) {
            var errMsg = "Дата обязательное поле";
            errMessages.add(errMsg);
        }
        if (bookingDto.getEnd().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            var errMsg = "Дата окончания не может быть в прошлом";
            errMessages.add(errMsg);
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            var errMsg = "Дата окончания должна быть позже даты начала";
            errMessages.add(errMsg);
        }
        if (bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            var errMsg = "Дата окончания должна быть позже даты начала";
            errMessages.add(errMsg);
        }
        if (bookingDto.getStart().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            var errMsg = "Дата начала должна быть позже текущей даты";
            errMessages.add(errMsg);
        }
        return errMessages;
    }
}