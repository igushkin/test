package ru.practicum.shareit.booking.dto;

import org.apache.logging.log4j.util.Strings;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class BookingDTOValidator {
    private BookingDTOValidator() {
    }

    public static List<String> validateCreation(BookingDto bookingDto) {
        List<String> errMessages = new ArrayList<>();

        if (bookingDto.getStart() == null) {
            errMessages.add("Дата начала обязательное поле");
        }
        if (bookingDto.getEnd() == null) {
            errMessages.add("Дата окончания обязательне поле");
        }
        if (bookingDto.getEnd().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            errMessages.add("Дата окончания не может быть в прошлом");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            errMessages.add("Дата окончания должна быть позже даты начала");
        }
        if (bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            errMessages.add("Дата окончания должна быть позже даты начала");
        }
        if (bookingDto.getStart().toInstant(ZoneOffset.UTC).isBefore(Instant.now())) {
            errMessages.add("Дата начала должна быть позже текущей начала");
        }
        return errMessages;
    }

    public static List<String> validatePatch(ItemDto itemDto) {
        List<String> errMessages = new ArrayList<>();

        if (itemDto.getName() != null && Strings.isEmpty(itemDto.getName())) {
            errMessages.add("Имя не может быть пустым");
        }
        if (itemDto.getDescription() != null && Strings.isEmpty(itemDto.getDescription())) {
            errMessages.add("Описание не может быть пустым");
        }

        return errMessages;
    }
}