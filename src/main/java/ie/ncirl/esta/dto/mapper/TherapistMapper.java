package ie.ncirl.esta.dto.mapper;

import ie.ncirl.esta.dto.TherapistDto;
import ie.ncirl.esta.model.Therapist;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TherapistMapper {

    public static Therapist toEntity(TherapistDto dto) {
        return Therapist.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .createdOn(dto.getCreatedOn())
                .confirmed(dto.getConfirmed())
                .build();
    }

    public static TherapistDto toDto(Therapist entity) {
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
