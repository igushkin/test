package ru.practicum.shareit.IntegrationalTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTests {

    private final EntityManager em;
    private final UserService userService;

    // getUsers
    // getUserById
    // createUser
    // patchUser
    // deleteUser

    @Test
    void getUsers() {
        var user = User.builder()
                .name("Mark")
                .email("email@email.com")
                .build();

        em.persist(user);

        var result = userService.getUsers();

        Assertions.assertEquals(result.size(), 1);
    }

    @Test
    void getUserById() {
        var user = User.builder()
                .name("Mark")
                .email("email@email.com")
                .build();

        em.persist(user);

        var result = userService.getUserById(user.getId());

        Assertions.assertNotNull(result);
    }

    @Test
    void createUser() {
        var user = User.builder()
                .name("Mark")
                .email("email@email.com")
                .build();

        var result = userService.createUser(UserMapper.toUserDto(user));

        Assertions.assertNotNull(result);

        Assertions.assertThrows(Exception.class, () -> userService.createUser(UserMapper.toUserDto(user)));
    }

    @Test
    void patchUser() {
        var user = User.builder()
                .name("Mark")
                .email("email@email.com")
                .build();

        em.persist(user);

        user.setName("new name");

        var result = userService.patchUser(UserMapper.toUserDto(user), user.getId());

        Assertions.assertEquals(user.getName(), "new name");
    }

    @Test
    void deleteUser() {
        var user = User.builder()
                .name("Mark")
                .email("email@email.com")
                .build();

        em.persist(user);
        userService.deleteUser(user.getId());

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        var dbResult = query.setParameter("id", user.getId()).getResultList();

        Assertions.assertNotNull(dbResult.size() == 0);
    }
}