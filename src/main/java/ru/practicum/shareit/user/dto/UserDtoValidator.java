package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserDtoValidator {
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