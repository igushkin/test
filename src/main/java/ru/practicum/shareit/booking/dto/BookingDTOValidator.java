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
            var errMsg = "Дата";
            errMsg += "начала";
            errMsg += "обязательное";
            errMsg += "поле";
            errMessages.add(errMsg);
        }
        if (bookingDto.getEnd() == null) {
            var errMsg = "Дата";
            errMsg += "начала";
            errMsg += "обязательное";
            errMsg += "поле";
            errMessages.add(errMsg);
        }
        if (bookingDto.getEnd().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            var errMsg = "Дата";
            errMsg += "окончания";
            errMsg += "не";
            errMsg += "может";
            errMsg += "быть";
            errMsg += "в";
            errMsg += "прошлом";
            errMessages.add(errMsg);
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            var errMsg = "Дата";
            errMsg += "окончания";
            errMsg += "должна";
            errMsg += "быть";
            errMsg += "позже";
            errMsg += "даты";
            errMsg += "начала";
            errMessages.add(errMsg);
        }
        if (bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            var errMsg = "Дата";
            errMsg += "окончания";
            errMsg += "должна";
            errMsg += "быть";
            errMsg += "позже";
            errMsg += "даты";
            errMsg += "начала";
            errMessages.add(errMsg);
        }
        if (bookingDto.getStart().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            var errMsg = "Дата";
            errMsg += "начала";
            errMsg += "должна";
            errMsg += "быть";
            errMsg += "позже";
            errMsg += "текущей";
            errMsg += "даты";
            errMessages.add(errMsg);
        }
        return errMessages;
    }
}