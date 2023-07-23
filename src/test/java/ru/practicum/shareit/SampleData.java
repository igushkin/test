package ru.practicum.shareit;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SampleData {
    public static List<User> users;
    public static List<Booking> futureBookings;
    public static List<Booking> pastBookings;
    public static List<Item> items;

    public static List<Comment> comments;

    static {
        fillUsers();
        fillFutureBookings();
        fillPastBookings();
        fillItems();
        fillComments();
    }

    public static void resetData() {
        fillUsers();
        fillFutureBookings();
        fillPastBookings();
        fillItems();
        fillComments();
    }

    private static void fillUsers() {
        var user1 = User.builder()
                .id(1)
                .name("name1")
                .email("email1")
                .build();

        var user2 = User.builder()
                .id(2)
                .name("name2")
                .email("email2")
                .build();
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
    }

    private static void fillPastBookings() {
        var booking1 = Booking.builder()
                .id(1)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusHours(1))
                .build();

        var booking2 = Booking.builder()
                .id(2)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusHours(2))
                .build();

        pastBookings = new ArrayList<>();
        pastBookings.add(booking1);
        pastBookings.add(booking2);
    }

    private static void fillFutureBookings() {
        var booking3 = Booking.builder()
                .id(3)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        var booking4 = Booking.builder()
                .id(4)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        futureBookings = new ArrayList<>();
        futureBookings.add(booking3);
        futureBookings.add(booking4);
    }

    private static void fillItems() {
        var item1 = Item
                .builder()
                .id(1)
                .available(true)
                .name("name 1")
                .description("descr 1")
                .build();

        var item2 = Item
                .builder()
                .id(2)
                .available(true)
                .name("name 2")
                .description("descr 2")
                .build();

        items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
    }

    private static void fillComments() {
        var comment1 = Comment.builder()
                .id(1)
                .text("my first comment")
                .created(LocalDateTime.now())
                .build();

        var comment2 = Comment.builder()
                .id(2)
                .text("my second comment")
                .created(LocalDateTime.now())
                .build();

        comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
    }
}