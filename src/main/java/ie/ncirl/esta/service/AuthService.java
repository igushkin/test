package ie.ncirl.esta.service;

import ie.ncirl.esta.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto attemptLogin(String login, String password);
}
