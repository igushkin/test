package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public interface UserRepository extends JpaRepository<User, Integer> {

}
