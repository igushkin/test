package ie.ncirl.esta.service;

import ie.ncirl.esta.dto.CarerDto;
import ie.ncirl.esta.dto.ChildDto;
import ie.ncirl.esta.dto.TherapistDto;

import java.util.List;
import java.util.Optional;

public interface TherapistService {
    List<TherapistDto> findAll();

    Optional<TherapistDto> findById(Long id);

    Optional<TherapistDto> findByEmail(String email);

    CarerDto createCarer(Long therapistId, CarerDto carerDto);

    ChildDto createChild(Long carerId, ChildDto childDto);


}
