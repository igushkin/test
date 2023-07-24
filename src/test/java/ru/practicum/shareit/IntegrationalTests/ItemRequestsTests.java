package ru.practicum.shareit.IntegrationalTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestsTests {

    private final EntityManager em;
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final UserRepository userRepository;

    // createItemRequest
    // getAllByUserId
    // getAll
    // getById

    @Test
    void createItemRequest() {

        var requestor = User.builder()
                .name("Mark")
                .email("s")
                .build();

        var itemRequest = ItemRequest.builder()
                .description("desc")
                .requestor(requestor)
                .build();

        em.persist(requestor);
        var result = itemRequestService.createItemRequest(requestor.getId(), ItemRequestMapper.toItemRequestDto(itemRequest));
        Assertions.assertEquals(result.getDescription(), itemRequest.getDescription());
    }

    @Test
    void getAllByUserId() {

        var requestor = User.builder()
                .name("Mark")
                .email("s")
                .build();

        var itemRequest = ItemRequest.builder()
                .description("desc")
                .requestor(requestor)
                .build();

        em.persist(requestor);
        em.persist(itemRequest);

        var result = itemRequestService.getAllByUserId(requestor.getId());
        Assertions.assertEquals(result.size(), 1);
    }

    @Test
    void getAll() {

        var user = User.builder()
                .name("User")
                .email("s")
                .build();

        var requestor = User.builder()
                .name("Mark")
                .email("sasdas")
                .build();

        var itemRequest = ItemRequest.builder()
                .description("desc")
                .requestor(requestor)
                .build();

        em.persist(user);
        em.persist(requestor);
        em.persist(itemRequest);

        var result = itemRequestService.getAll(user.getId(), null, null);
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertDoesNotThrow(() -> itemRequestService.getAll(user.getId(), 0, 10));
        Assertions.assertThrows(Exception.class, () -> itemRequestService.getAll(user.getId(), -1, -2));
    }

    @Test
    void getById() {

        var user = User.builder()
                .name("Mark")
                .email("s")
                .build();

        var itemRequest = ItemRequest.builder()
                .description("desc")
                .requestor(user)
                .build();

        em.persist(user);
        em.persist(itemRequest);

        var result = itemRequestService.getById(itemRequest.getId(), user.getId());
        Assertions.assertEquals(result.getDescription(), itemRequest.getDescription());
    }
}