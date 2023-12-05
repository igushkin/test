package ie.ncirl.esta.service;

import ie.ncirl.esta.dto.ChildDto;

import java.util.List;
import java.util.Optional;

public interface ChildService {

    List<ChildDto> findAll();

    Optional<ChildDto> findById(Long id);

    Optional<ChildDto> findByEmail(String email);
}
