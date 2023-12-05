package ie.ncirl.esta.dto;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {
    private String token;
}
