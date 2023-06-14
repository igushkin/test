package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    private Integer id;
    private String name;
    @NotBlank
    @Email
    private String email;
}