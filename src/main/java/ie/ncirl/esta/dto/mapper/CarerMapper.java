package ie.ncirl.esta.dto.mapper;

import ie.ncirl.esta.dto.CarerDto;
import ie.ncirl.esta.model.Carer;

public class CarerMapper {
    public static Carer toEntity(CarerDto dto) {
        return Carer.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .createdOn(dto.getCreatedOn())
                .confirmed(dto.getConfirmed())
                .build();
    }

    public static CarerDto toDto(Carer entity) {
        return CarerDto.builder()
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
