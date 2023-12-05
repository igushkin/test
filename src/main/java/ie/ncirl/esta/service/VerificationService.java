package ie.ncirl.esta.service;

import ie.ncirl.esta.dto.UserDto;

public interface VerificationService {
    boolean sendVerificationRequest(Long userId, String email);

    UserDto verifyAccount(Integer verificationCode, String password);
}
