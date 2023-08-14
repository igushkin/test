package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingExtendedDto extends BookingDto {
    private ItemDto item;
    private UserDto booker;

    public BookingExtendedDto(Integer bookingId, LocalDateTime start, LocalDateTime end, ItemDto itemDto, UserDto userDto, BookingState status) {
        super(bookingId, start, end, status, itemDto.getId(), userDto.getId());
        this.item = itemDto;
        this.booker = userDto;
    }
}