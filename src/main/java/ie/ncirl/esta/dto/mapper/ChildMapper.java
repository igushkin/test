package ie.ncirl.esta.dto.mapper;

import ie.ncirl.esta.dto.ChildDto;
import ie.ncirl.esta.model.Child;

public class ChildMapper {
    public static Child toEntity(ChildDto dto) {
        return Child.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .createdOn(dto.getCreatedOn())
                .confirmed(dto.getConfirmed())
                .build();
    }

    public static ChildDto toDto(Child entity) {
        return ChildDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .userName(entity.getUserName())
                .password(entity.getPassword())
                .createdOn(entity.getCreatedOn())
                .confirmed(entity.getConfirmed())
                .build();
    }
}
