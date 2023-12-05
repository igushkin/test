package ie.ncirl.esta.security;

import ie.ncirl.esta.model.*;
import ie.ncirl.esta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user;

        try {
            user = userRepository.findUserByUserName(username).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User was not found");
        }

        String role;

        if (user.getClass().equals(Admin.class)) {
            role = "ROLE_ADMIN";
        } else if (user.getClass().equals((Therapist.class))) {
            role = "ROLE_THERAPIST";
        } else if (user.getClass().equals(Carer.class)) {
            role = "ROLE_CARER";
        } else if (user.getClass().equals(Child.class)) {
            role = "ROLE_CHILD";
        } else if (user.getClass().equals(User.class)) {
            role = "ROLE_USER";
        } else {
            throw new RuntimeException("Wrong user type");
        }

        return UserPrincipal.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(role)))
                .build();
    }
}