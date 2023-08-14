package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    protected Integer id;
    protected LocalDateTime start;
    protected LocalDateTime end;
    protected BookingState status;
    private Integer itemId;
    private Integer bookerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDto that = (BookingDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                status == that.status &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(bookerId, that.bookerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, status, itemId, bookerId);
    }
}