package ie.ncirl.esta.service;

import ie.ncirl.esta.dto.AdminDto;
import ie.ncirl.esta.dto.TherapistDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AdminService {

    void init();

    List<AdminDto> findAll();

    Optional<AdminDto> findById(Long id);

    Optional<AdminDto> findByEmail(String email);

    TherapistDto createTherapist(TherapistDto therapistDto);
}

