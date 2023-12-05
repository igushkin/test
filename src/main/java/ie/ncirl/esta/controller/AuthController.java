package ie.ncirl.esta.controller;

import ie.ncirl.esta.dto.LoginDto;
import ie.ncirl.esta.dto.LoginResponseDto;
import ie.ncirl.esta.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto dto, HttpServletResponse httpServletResponse) {
        return authService.attemptLogin(dto.getUserName(), dto.getPassword());
    }
}
