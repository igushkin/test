package ie.ncirl.esta.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ie.ncirl.esta.security.SecretKey;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    public String issue(Request request) {
        return JWT.create()
                .withSubject(String.valueOf(request.userId))
                .withClaim("e", request.getUserName())
                .withClaim("au", request.getRoles())
                .sign(Algorithm.HMAC256(SecretKey.MY_SECRET));
    }

    @Getter
    @Builder
    public static class Request {
        private final Long userId;
        private final String userName;
        private final List<String> roles;
    }
}