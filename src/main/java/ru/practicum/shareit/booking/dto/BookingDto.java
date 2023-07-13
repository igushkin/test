package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    protected Integer id;
    protected LocalDateTime start;
    protected LocalDateTime end;
    protected BookingStatus status;
    private Integer itemId;
    private Integer bookerId;
}