package ie.ncirl.esta.service.impl;

import ie.ncirl.esta.dto.UserDto;
import ie.ncirl.esta.dto.mapper.UserMapper;
import ie.ncirl.esta.model.*;
import ie.ncirl.esta.repository.*;
import ie.ncirl.esta.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    private final AdminRepository adminRepository;
    private final TherapistRepository therapistRepository;
    private final ChildRepository childRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationRepository verificationRepository;
    private final CarerRepository carerRepository;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper::toDto).toList();
    }

    @Override
    public void init() {
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
        child.setEmail("u2@gmail.com");
        child.setUserName("u2");
        child.setPassword(passwordEncoder.encode("1"));
        childRepository.save(child);

        var th = new Therapist();
        th.setConfirmed(true);
        th.setCreatedOn(Instant.now());
        th.setFirstName("z");
        th.setLastName("x");
        th.setEmail("u3@gmail.com");
        th.setUserName(th.getEmail());
        th.setPassword(passwordEncoder.encode("1"));
        therapistRepository.save(th);


        var carer = new Carer();
        carer.setConfirmed(true);
        carer.setCreatedOn(Instant.now());
        carer.setFirstName("z");
        carer.setLastName("x");
        carer.setEmail("u4@gmail.com");
        carer.setUserName(carer.getEmail());
        carer.setPassword(passwordEncoder.encode("1"));
        carerRepository.save(carer);
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        var user = userRepository.findById(id);

        if (user.isPresent()) {
            return Optional.of(UserMapper.toDto(user.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        var user = userRepository.findUserByEmail(email);

        if (user.isPresent()) {
            return Optional.of(UserMapper.toDto(user.get()));
        } else {
            return Optional.empty();
        }
    }

/*    @Override
    public UserDto approveAccount(Integer verificationCode, UserDto dto) {
        var ver = verificationRepository.findById(verificationCode).get();
        var user = userRepository.findById(ver.getUser().getId()).get();
        if (user.getConfirmed()) {
            throw new RuntimeException("Already confirmed");
        }
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setConfirmed(true);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }*/

    private Integer generateHash(String email) {
        return String.join(Strings.EMPTY, email, Instant.now().toString()).hashCode();
    }

    private Verification saveVerificationCode(User user, Integer hash) {
        return verificationRepository.save(
                Verification.builder()
                        .id(hash)
                        .createdOn(Instant.now())
                        .user(user)
                        .build());
    }
}