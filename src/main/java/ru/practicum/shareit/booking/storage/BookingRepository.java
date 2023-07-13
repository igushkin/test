package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "JOIN FETCH b.item " +
            "WHERE b.booker.id = ?1 and (?2 is null or b.status = ?2) " +
            "ORDER BY b.start desc")
    List<Booking> findAllBookingsByUserIdAndStatusWithFetch(Integer userId, String bookingStatus);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "JOIN FETCH b.item " +
            "WHERE b.booker.id = ?1 and b.end < ?2 " +
            "ORDER BY b.start desc ")
    List<Booking> findAllPastBookingsByUserIdWithFetch(Integer userId, LocalDateTime localDateTime);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "JOIN FETCH b.item " +
            "WHERE b.booker.id = ?1 and b.start < ?2 and b.end > ?2 " +
            "ORDER BY b.start desc ")
    List<Booking> findAllCurrentBookingsByUserIdWithFetch(Integer userId, LocalDateTime localDateTime);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "JOIN FETCH b.item " +
            "WHERE b.booker.id = ?1 and b.start > ?2 " +
            "ORDER BY b.start desc")
    List<Booking> findAllFutureBookingsByUserIdWithFetch(Integer userId, LocalDateTime localDateTime);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "JOIN FETCH b.item " +
            "WHERE b.item.owner.id = ?1 and (?2 is null or b.status = ?2) " +
            "ORDER BY b.start desc")
    List<Booking> findAllBookingsByOwnerId(Integer userId, String bookingStatus);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "JOIN FETCH b.item " +
            "WHERE b.item.owner.id = ?1 and b.end < ?2 " +
            "ORDER BY b.start desc")
    List<Booking> findAllPastBookingsByOwnerIdWithFetch(Integer userId, LocalDateTime localDateTime);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "JOIN FETCH b.item " +
            "WHERE b.item.owner.id = ?1 and b.start < ?2 and b.end > ?2 " +
            "ORDER BY b.start desc")
    List<Booking> findAllCurrentBookingsByOwnerIdWithFetch(Integer userId, LocalDateTime localDateTime);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.booker " +
            "JOIN FETCH b.item " +
            "WHERE b.item.owner.id = ?1 and b.start > ?2 " +
            "ORDER BY b.start desc")
    List<Booking> findAllFutureBookingsByOwnerIdWithFetch(Integer userId, LocalDateTime localDateTime);

    @Query("select i " +
            "from Item i " +
            "JOIN FETCH i.owner " +
            "WHERE i.owner.id = ?1 ")
    List<Item> findOwnerById(Integer userId);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "WHERE b.start < ?2 and b.item.id = ?1 and b.status = 'APPROVED' " +
            "ORDER BY b.start desc")
    List<Booking> findLastBookingByItemId(Integer itemId, LocalDateTime localDateTime, Pageable pageable);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "WHERE b.start > ?2 and b.item.id = ?1 and b.status = 'APPROVED' " +
            "ORDER BY b.start ")
    List<Booking> findNextBookingByItemId(Integer itemId, LocalDateTime localDateTime, Pageable pageable);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "WHERE b.item.id in ?1 and b.status ='APPROVED' ")
    List<Booking> findAllBookingsByItemIds(List<Integer> itemId);

    @Query(" select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.booker.id = ?1 and b.item.id = ?2 and b.end < ?3 ")
    List<Booking> findByBookerIdAndItemIdAndEndBefore(Integer bookerId, Integer itemId, LocalDateTime start);
}