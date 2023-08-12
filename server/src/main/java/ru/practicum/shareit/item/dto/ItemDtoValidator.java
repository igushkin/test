package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemDtoValidator {
    public static List<String> validateCreation(ItemDto itemDto) {
        List<String> errMessages = new ArrayList<>();

        if (Strings.isBlank(itemDto.getName())) {
            errMessages.add("Имя не может быть пустым");
        }
        if (Strings.isBlank(itemDto.getDescription())) {
            errMessages.add("Описание не может быть пустым");
        }
        if (itemDto.getAvailable() == null) {
            errMessages.add("Статус не может быть пустым");
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