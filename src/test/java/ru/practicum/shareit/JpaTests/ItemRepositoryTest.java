package ru.practicum.shareit.JpaTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void testSavings() {
        Item item = new Item();
        item.setName("MyItem");
        item.setDescription("desc");
        item.setAvailable(true);

        item = itemRepository.save(item);

        Assertions.assertEquals(itemRepository.findAll().size(), 1);
    }

    @Test
    void testFindByOwnerId() {
        var user = new User();
        user.setName("mark");
        user.setEmail("email");

        var item = new Item();
        item.setName("MyItem");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(user);

        user = em.persist(user);
        item = em.persist(item);

        var result = itemRepository.findByOwnerId(user.getId());

        Assertions.assertEquals(result.size(), 1);
    }

    @Test
    void testFindAllByRequestIdIn() {
        var user = new User();
        user.setName("mark");
        user.setEmail("email");

        var request = new ItemRequest();
        request.setDescription("desc");
        request.setRequestor(user);

        var item = new Item();
        item.setName("MyItem");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(request);

        user = em.persist(user);
        item = em.persist(item);
        request = em.persist(request);

        var result = itemRepository.findAllByRequestIdIn(List.of(request.getId()));

        Assertions.assertEquals(result.size(), 1);
    }
}