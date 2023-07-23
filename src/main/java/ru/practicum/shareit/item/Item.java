package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
    @ManyToOne()
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private ItemRequest request;
    /*    @ManyToOne()
        @JoinColumn(name = "item_request_id", referencedColumnName = "id")
        private ItemRequest itemRequest;*/
    @Transient
    private Booking lastBooking;
    @Transient
    private Booking nextBooking;
}