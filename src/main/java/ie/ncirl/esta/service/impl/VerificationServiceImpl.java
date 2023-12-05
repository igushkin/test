package ie.ncirl.esta.service.impl;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import ie.ncirl.esta.dto.UserDto;
import ie.ncirl.esta.dto.mapper.UserMapper;
import ie.ncirl.esta.model.User;
import ie.ncirl.esta.model.Verification;
import ie.ncirl.esta.repository.UserRepository;
import ie.ncirl.esta.repository.VerificationRepository;
import ie.ncirl.esta.service.EmailService;
import ie.ncirl.esta.service.VerificationService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public boolean sendVerificationRequest(Long userId, String email) {
        var user = userRepository.findById(userId).get();
        var hash = generateHash(email);

        var ver = Verification.builder()
                .id(hash)
                .createdOn(Instant.now())
                .user(user)
                .build();

        verificationRepository.save(ver);

        try {
            emailService.sendVerificationEmail(user.getFirstName(), user.getLastName(), email, hash);
        } catch (MailjetException | MailjetSocketTimeoutException e) {
            return false;
        }
        return true;
    }

    @Override
    public UserDto verifyAccount(Integer verificationCode, String password) {
        var ver = verificationRepository.findById(verificationCode).get();
        var user = userRepository.findById(ver.getUser().getId()).get();

        if (user.getConfirmed()) {
            throw new RuntimeException("Already confirmed");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setConfirmed(true);

        userRepository.save(user);
        return UserMapper.toDto(user);
    }

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
