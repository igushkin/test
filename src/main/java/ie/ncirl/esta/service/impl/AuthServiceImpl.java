package ie.ncirl.esta.service.impl;

import ie.ncirl.esta.dto.LoginResponseDto;
import ie.ncirl.esta.security.UserPrincipal;
import ie.ncirl.esta.security.jwt.JwtIssuer;
import ie.ncirl.esta.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtIssuer jwtIssuer;

    public LoginResponseDto attemptLogin(String login, String password) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var principal = (UserPrincipal) authentication.getPrincipal();

        var token = jwtIssuer.issue(JwtIssuer.Request.builder()
                .userId(principal.getId())
                .userName(principal.getUsername())
                .roles(principal.getAuthorities().stream().map(auth -> auth.getAuthority()).toList())
                .build());

        return LoginResponseDto.builder()
                .token(token)
                .build();
    }
}
