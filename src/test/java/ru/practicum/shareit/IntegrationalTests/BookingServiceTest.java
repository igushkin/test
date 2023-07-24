package ru.practicum.shareit.IntegrationalTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {

    private final EntityManager em;
    private final BookingService bookingService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Test
    void createBooking() {
        var owner = User.builder()
                .name("Mark")
                .email("s")
                .build();

        var booker = User.builder()
                .name("Booker")
                .email("a")
                .build();

        var item = Item.builder()
                .name("item")
                .available(true)
                .description("desc")
                .owner(owner)
                .build();

        var booking = Booking.builder()
                .item(item)
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusMinutes(5))
                .booker(booker)
                .build();

        em.persist(owner);
        em.persist(booker);
        em.persist(item);

        Assertions.assertDoesNotThrow(() -> bookingService.createBooking(booker.getId(), BookingMapper.toBookingDto(booking)));

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.booker.name = :bookerName", Booking.class);
        var dbResult = query.setParameter("bookerName", booker.getName()).getSingleResult();

        Assertions.assertEquals(dbResult.getBooker().getName(), booker.getName());
    }

    @Test
    void getBookingsByOwnerId() {
        var owner = User.builder()
                .name("Mark")
                .email("s")
                .build();

        var booker = User.builder()
                .name("Booker")
                .email("a")
                .build();

        var item = Item.builder()
                .name("item")
                .available(true)
                .description("desc")
                .owner(owner)
                .build();

        var booking = Booking.builder()
                .item(item)
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusMinutes(5))
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        var result = bookingService.getBookingsByOwnerId(owner.getId(), BookingState.ALL, null, null);
        Assertions.assertEquals(result.size(), 1);

        result = bookingService.getBookingsByOwnerId(owner.getId(), BookingState.WAITING, null, null);
        Assertions.assertEquals(result.size(), 1);

        result = bookingService.getBookingsByOwnerId(owner.getId(), BookingState.REJECTED, null, null);
        Assertions.assertEquals(result.size(), 0);

        result = bookingService.getBookingsByOwnerId(owner.getId(), BookingState.PAST, null, null);
        Assertions.assertEquals(result.size(), 0);

        result = bookingService.getBookingsByOwnerId(owner.getId(), BookingState.FUTURE, null, null);
        Assertions.assertEquals(result.size(), 1);

        result = bookingService.getBookingsByOwnerId(owner.getId(), BookingState.CURRENT, null, null);
        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    void getBookingById() {
        var owner = User.builder()
                .name("Mark")
                .email("s")
                .build();

        var booker = User.builder()
                .name("Booker")
                .email("a")
                .build();

        var item = Item.builder()
                .name("item")
                .available(true)
                .description("desc")
                .owner(owner)
                .build();

        var booking = Booking.builder()
                .item(item)
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusMinutes(5))
                .booker(booker)
                .build();

        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        var resultByOwner = bookingService.getBookingById(owner.getId(), booking.getId());
        var resultByBooker = bookingService.getBookingById(booker.getId(), booking.getId());

        Assertions.assertThrows(Exception.class, () -> bookingService.getBookingById(-1, booking.getId()));
        Assertions.assertNotNull(resultByOwner);
        Assertions.assertNotNull(resultByBooker);
    }

    @Test
    void getAllBookingsByUserId() {
        var owner = User.builder()
                .name("Mark")
                .email("s")
                .build();

        var booker = User.builder()
                .name("Booker")
                .email("a")
                .build();

        var item = Item.builder()
                .name("item")
                .available(true)
                .description("desc")
                .owner(owner)
                .build();

        var booking = Booking.builder()
                .item(item)
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusMinutes(5))
                .booker(booker)
                .build();

        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        Assertions.assertDoesNotThrow(() -> bookingService.getAllBookingsByUserId(owner.getId(), BookingState.ALL, null, null));
        Assertions.assertDoesNotThrow(() -> bookingService.getAllBookingsByUserId(owner.getId(), BookingState.WAITING, null, null));
        Assertions.assertDoesNotThrow(() -> bookingService.getAllBookingsByUserId(owner.getId(), BookingState.REJECTED, null, null));
        Assertions.assertDoesNotThrow(() -> bookingService.getAllBookingsByUserId(owner.getId(), BookingState.PAST, null, null));
        Assertions.assertDoesNotThrow(() -> bookingService.getAllBookingsByUserId(owner.getId(), BookingState.FUTURE, null, null));
        Assertions.assertDoesNotThrow(() -> bookingService.getAllBookingsByUserId(owner.getId(), BookingState.CURRENT, null, null));
    }

    @Test
    void setBookingStatus() {
        var owner = User.builder()
                .name("Mark")
                .email("s")
                .build();

        var booker = User.builder()
                .name("Booker")
                .email("a")
                .build();

        var item = Item.builder()
                .name("item")
                .available(true)
                .description("desc")
                .owner(owner)
                .build();

        var booking = Booking.builder()
                .item(item)
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusMinutes(5))
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);

        Assertions.assertDoesNotThrow(() -> bookingService.setBookingStatus(owner.getId(), booking.getId(), BookingStatus.APPROVED));
        Assertions.assertThrows(Exception.class, () -> bookingService.setBookingStatus(-1, booking.getId(), BookingStatus.APPROVED));
    }
}