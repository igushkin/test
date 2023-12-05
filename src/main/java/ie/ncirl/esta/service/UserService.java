package ie.ncirl.esta.service;

import ie.ncirl.esta.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void init();

    List<UserDto> findAll();

    Optional<UserDto> findById(Long id);

    Optional<UserDto> findByEmail(String email);
}
