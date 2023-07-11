package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
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