package ie.ncirl.esta.dto.mapper;

import ie.ncirl.esta.dto.TherapistDto;
import ie.ncirl.esta.dto.UserDto;
import ie.ncirl.esta.model.Therapist;
import ie.ncirl.esta.model.User;

public class UserMapper {
    public static User toEntity(UserDto dto) {
        return Therapist.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .userName(dto.getUserName())
                .build();
    }

    public static UserDto toDto(User entity) {
        return TherapistDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .userName(entity.getUserName())
                .password(entity.getPassword())
                .createdOn(entity.getCreatedOn())
                .confirmed(entity.getConfirmed())
                .build();
    }
}
