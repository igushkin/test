package ie.ncirl.esta.service.impl;

import ie.ncirl.esta.dto.AdminDto;
import ie.ncirl.esta.dto.TherapistDto;
import ie.ncirl.esta.dto.UserDto;
import ie.ncirl.esta.dto.mapper.AdminMapper;
import ie.ncirl.esta.dto.mapper.TherapistMapper;
import ie.ncirl.esta.model.Admin;
import ie.ncirl.esta.model.Child;
import ie.ncirl.esta.model.Therapist;
import ie.ncirl.esta.repository.AdminRepository;
import ie.ncirl.esta.repository.ChildRepository;
import ie.ncirl.esta.repository.TherapistRepository;
import ie.ncirl.esta.service.AdminService;
import ie.ncirl.esta.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ChildRepository childRepository;
    private final PasswordEncoder passwordEncoder;
    private final TherapistRepository therapistRepository;
    private final VerificationService verificationService;

    @Override
    public void init() {

        var zz = UserDto.builder().firstName("asd").lastName("asd").email("sad.asd@asd.ru").build();

        var admin = new Admin();
        admin.setConfirmed(true);
        admin.setCreatedOn(Instant.now());
        admin.setFirstName("z");
        admin.setLastName("x");
        admin.setEmail("u1@gmail.com");
        admin.setUserName(admin.getEmail());
        admin.setPassword(passwordEncoder.encode("1"));
        adminRepository.save(admin);

        var child = new Child();
        child.setConfirmed(true);
        child.setCreatedOn(Instant.now());
        child.setFirstName("z");
        child.setLastName("x");
        child.setUserName("u2@gmail.com");
        childRepository.save(child);

        var th = new Therapist();
        th.setConfirmed(true);
        th.setCreatedOn(Instant.now());
        th.setFirstName("z");
        th.setLastName("x");
        th.setEmail("u3@gmail.com");
        th.setUserName(th.getEmail());
        therapistRepository.save(th);
    }

    @Override
    public TherapistDto createTherapist(TherapistDto therapistDto) {
        var therapist = TherapistMapper.toEntity(therapistDto);

        therapist.setUserName(therapist.getEmail());
        therapist.setCreatedOn(Instant.now());
        therapist.setConfirmed(false);
        therapist = therapistRepository.save(therapist);

        verificationService.sendVerificationRequest(therapist.getId(), therapist.getEmail());

        return TherapistMapper.toDto(therapist);
    }

    @Override
    public List<AdminDto> findAll() {
        return adminRepository.findAll()
                .stream()
                .map(AdminMapper::toDto)
                .toList();
    }

    @Override
    public Optional<AdminDto> findById(Long id) {
        var admin = adminRepository.findById(id);

        if (admin.isPresent()) {
            return Optional.of(AdminMapper.toDto(admin.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AdminDto> findByEmail(String email) {
        var admin = adminRepository.findAdminByEmail(email);

        if (admin.isPresent()) {
            return Optional.of(AdminMapper.toDto(admin.get()));
        } else {
            return Optional.empty();
        }
    }
}
