package ru.practicum.shareit.UnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.SampleData;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;
    BookingService bookingService;

    @BeforeEach
    private void resetSampleDate() {
        SampleData.resetData();
        this.bookingService = new BookingService(bookingRepository, userRepository, itemRepository);
    }

    @Test
    void invalidCreation() {
        var booker = User.builder().id(1).build();
        var item = Item.builder().id(1).owner(booker).build();
        var bookingDto = BookingMapper.toBookingDto(Booking.builder()
                .booker(booker)
                .item(item)
                .build());

        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booker));

        Assertions.assertThrows(Exception.class, () -> bookingService.createBooking(1, bookingDto));
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        Assertions.assertThrows(Exception.class, () -> bookingService.createBooking(1, bookingDto));
        bookingDto.setEnd(LocalDateTime.now().minusDays(2));
        Assertions.assertThrows(Exception.class, () -> bookingService.createBooking(1, bookingDto));
        bookingDto.setEnd(bookingDto.getStart());
        Assertions.assertThrows(Exception.class, () -> bookingService.createBooking(1, bookingDto));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        Assertions.assertThrows(Exception.class, () -> bookingService.createBooking(1, bookingDto));

    }
}