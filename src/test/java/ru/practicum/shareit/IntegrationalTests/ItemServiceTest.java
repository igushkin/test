package ru.practicum.shareit.IntegrationalTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Test
    void saveItem() {
        var user = User.builder()
                .name("Mark")
                .email("email@email.com")
                .build();

        var userId = userService.createUser(UserMapper.toUserDto(user)).getId();

        ItemDto itemDto = ItemDto.builder()
                .name("New item 123")
                .description("Item desc")
                .available(true)
                .build();

        var itemId = itemService.createItem(userId, itemDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :itemName", Item.class);
        ItemDto result = ItemMapper.toItemDto(query.setParameter("itemName", itemDto.getName()).getSingleResult());

        Assertions.assertEquals(itemDto.getName(), result.getName());
        Assertions.assertEquals(itemDto.getDescription(), result.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), result.getAvailable());


        itemDto.setRequestId(1);
        Assertions.assertThrows(Exception.class, () -> itemService.createItem(userId, itemDto));
    }

    @Test
    void updateItem() {
        var user = User.builder()
                .name("Mark")
                .email("email@email.com")
                .build();

        var userId = userService.createUser(UserMapper.toUserDto(user)).getId();

        ItemDto itemDto = ItemDto.builder()
                .name("New item 123")
                .description("Item desc")
                .available(true)
                .build();

        var itemId = itemService.createItem(userId, itemDto).getId();

        itemDto.setName("New name");
        itemDto.setDescription("New description");

        var result = itemService.patchItem(itemDto, itemId, userId);

        Assertions.assertEquals(itemDto.getName(), result.getName());
        Assertions.assertEquals(itemDto.getDescription(), result.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), result.getAvailable());

        Assertions.assertThrows(Exception.class, () -> itemService.patchItem(itemDto, itemId, -2));
    }

    @Test
    void createComment() {
        var user = User.builder()
                .name("Mark")
                .email("email@email.com")
                .build();

        var userId = userService.createUser(UserMapper.toUserDto(user)).getId();

        ItemDto itemDto = ItemDto.builder()
                .name("New item 123")
                .description("Item desc")
                .available(true)
                .build();

        var itemId = itemService.createItem(userId, itemDto).getId();

        var comment = Comment.builder()
                .text("My first comment")
                .build();

        // Nobody booked this item
        Assertions.assertThrows(InvalidParameterException.class, () -> itemService.createComment(userId, itemId, comment));

        var usr = userRepository.findById(userId).get();
        var itm = itemRepository.findById(itemId).get();
        var booker = User.builder().name("booker").email("asd@asd.com").build();
        var booking = Booking.builder().item(itm).booker(booker).end(LocalDateTime.now().minusHours(2)).build();
        em.persist(booker);
        em.persist(booking);

        Assertions.assertDoesNotThrow(() -> itemService.createComment(booker.getId(), itemId, comment));
    }

    @Test
    void findItemByText() {
        Item item1 = Item.builder()
                .name("My book is good")
                .description("desc")
                .available(true)
                .build();

        Item item2 = Item.builder()
                .name("name")
                .description("My book is good")
                .available(true)
                .build();

        em.persist(item1);
        em.persist(item2);

        var res = itemService.findItemByText("book");

        Assertions.assertEquals(res.size(), 2);
    }

    @Test
    void getAllByUserId() {
        var user = User.builder()
                .name("Mark")
                .email("Email")
                .build();

        Item item1 = Item.builder()
                .name("My book is good")
                .description("desc")
                .available(true)
                .owner(user)
                .build();

        Item item2 = Item.builder()
                .name("name")
                .description("My book is good")
                .available(true)
                .owner(user)
                .build();

        em.persist(user);
        em.persist(item1);
        em.persist(item2);

        var res = itemService.getAllByUserId(user.getId());

        Assertions.assertEquals(res.size(), 2);
    }

    @Test
    void getItemById() {
        var user = User.builder()
                .name("Mark")
                .email("Email")
                .build();

        Item item = Item.builder()
                .name("My book is good")
                .description("desc")
                .available(true)
                .build();
        item.setOwner(user);

        em.persist(user);
        em.persist(item);

        var res = itemService.getItemById(item.getId(), user.getId());

        Assertions.assertEquals(res.getName(), item.getName());
    }
}