package ru.practicum.shareit.IntegrationalTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.security.InvalidParameterException;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTests {

    private final EntityManager em;
    private final ItemService service;
    private final UserService userService;
    private final UserRepository userRepository;

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

        var itemId = service.createItem(userId, itemDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :itemName", Item.class);
        ItemDto result = ItemMapper.toItemDto(query.setParameter("itemName", itemDto.getName()).getSingleResult());

        Assertions.assertEquals(itemDto.getName(), result.getName());
        Assertions.assertEquals(itemDto.getDescription(), result.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), result.getAvailable());
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

        var itemId = service.createItem(userId, itemDto).getId();

        itemDto.setName("New name");
        itemDto.setDescription("New description");

        var result = service.patchItem(itemDto, itemId, userId);

        Assertions.assertEquals(itemDto.getName(), result.getName());
        Assertions.assertEquals(itemDto.getDescription(), result.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), result.getAvailable());
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

        var itemId = service.createItem(userId, itemDto).getId();

        var comment = Comment.builder()
                .text("My first comment")
                .build();

        // Nobody booked this item
        Assertions.assertThrows(InvalidParameterException.class, () -> service.createComment(userId, itemId, comment));
    }
}