package ie.ncirl.esta.dto.mapper;

import ie.ncirl.esta.dto.AdminDto;
import ie.ncirl.esta.model.Admin;

public class AdminMapper {
    public static Admin toEntity(AdminDto dto) {
        return Admin.builder()
                .build();
    }

    public static AdminDto toDto(Admin entity) {
        return AdminDto.builder()
                .build();
    }
}
