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
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemSericeTests {
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserRepository userRepository;
    ItemService itemService;

    @BeforeEach
    private void resetSampleDate() {
        SampleData.resetData();
        this.itemService = new ItemService(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);
    }

    @Test
    public void getItemByIdByOwner() {
        var owner = SampleData.users.get(0);
        var booker = SampleData.users.get(1);
        var item = SampleData.items.get(0);
        item.setOwner(owner);

        SampleData.pastBookings.stream().forEach(x -> {
            x.setItem(item);
            x.setBooker(booker);
        });
        SampleData.futureBookings.stream().forEach(x -> {
            x.setItem(item);
            x.setBooker(booker);
        });

        var pastBookings = SampleData.pastBookings;
        var futureBookings = SampleData.futureBookings;

        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findBookingsBeforeDateAndByItemId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(pastBookings);
        Mockito.when(bookingRepository.findBookingsAfterDateAndByItemId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(futureBookings);
        Mockito.when(commentRepository.findByItemId(Mockito.any())).thenReturn(List.of());

        var result = itemService.getItemById(1, 1);

        Assertions.assertEquals(result, ItemMapper.toItemDto(item));
        Assertions.assertEquals(result.getLastBooking(), BookingMapper.toBookingDto(pastBookings.get(0)));
        Assertions.assertEquals(result.getNextBooking(), BookingMapper.toBookingDto(futureBookings.get(0)));
    }

    @Test
    public void getItemByIdByNotOwner() {
        var owner = SampleData.users.get(1);
        var booker = SampleData.users.get(0);
        var item = SampleData.items.get(0);
        item.setOwner(owner);

        SampleData.pastBookings.stream().forEach(x -> {
            x.setItem(item);
            x.setBooker(booker);
        });
        SampleData.futureBookings.stream().forEach(x -> {
            x.setItem(item);
            x.setBooker(booker);
        });

        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Mockito.when(commentRepository.findByItemId(Mockito.any())).thenReturn(List.of());

        var result = itemService.getItemById(1, 1);

        Assertions.assertEquals(result, ItemMapper.toItemDto(item));
        Assertions.assertEquals(result.getLastBooking(), null);
        Assertions.assertEquals(result.getNextBooking(), null);
    }

    @Test
    public void getItemsByUserId() {
        var owner = SampleData.users.get(0);
        var booker = SampleData.users.get(1);
        var items = SampleData.items;
        var bookings = SampleData.pastBookings;
        var comments = SampleData.comments;

        items.stream().forEach(x -> x.setOwner(owner));

        bookings.stream().forEach(x -> {
            x.setItem(items.get(0));
            x.setBooker(booker);
        });

        comments.stream().forEach(x -> {
            x.setItem(items.get(0));
            x.setAuthor(booker);
        });

        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(items);
        Mockito.when(bookingRepository.findAllBookingsByItemIds(Mockito.any())).thenReturn(bookings);
        Mockito.when(commentRepository.findByItemIdIn(Mockito.any())).thenReturn(comments);

        var result = itemService.getAllByUserId(1);
        var expectedItems = items.stream().map(x -> ItemMapper.toItemDto(x)).collect(Collectors.toList());
        var expectedComments = comments.stream().map(x -> CommentMapper.toCommentDto(x)).collect(Collectors.toList());
        var expectedBooking = BookingMapper.toBookingDto(bookings.get(0));

        Assertions.assertEquals(result, expectedItems);
        Assertions.assertEquals(result.get(0).getComments(), expectedComments);
        Assertions.assertEquals(result.get(0).getLastBooking(), expectedBooking);
    }
}