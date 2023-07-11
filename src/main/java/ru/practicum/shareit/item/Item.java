package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
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
    @Transient
    private Booking lastBooking;
    @Transient
    private Booking nextBooking;
}