package ru.practicum.shareit.user.dto;

import org.apache.logging.log4j.util.Strings;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

public class UserDtoValidator {
    private UserDtoValidator() {
    }

    public static List<String> validateCreation(UserDto userDto) {
        List<String> errMessages = new ArrayList<>();

        if (Strings.isBlank(userDto.getName())) {
            errMessages.add("Имя не может быть пустым");
        }
        if (Strings.isBlank(userDto.getEmail())) {
            errMessages.add("Почта не может быть пустым");
        }
        return errMessages;
    }

    public static List<String> validatePatch(UserDto userDto) {
        List<String> errMessages = new ArrayList<>();

        if (userDto.getName() != null && Strings.isEmpty(userDto.getName())) {
            errMessages.add("Имя не может быть пустым");
        }
        if (userDto.getEmail() != null && Strings.isEmpty(userDto.getEmail())) {
            errMessages.add("Почта не может быть пустым");
        }

        return errMessages;
    }
}
