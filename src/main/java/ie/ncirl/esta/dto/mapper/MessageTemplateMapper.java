package ie.ncirl.esta.dto.mapper;

import ie.ncirl.esta.dto.MessageTemplateDto;
import ie.ncirl.esta.model.MessageTemplate;

public class MessageTemplateMapper {
    public static MessageTemplate toEntity(MessageTemplateDto dto) {
        return MessageTemplate.builder()
                .build();
    }

    public static MessageTemplateDto toDto(MessageTemplate entity) {
        return MessageTemplateDto.builder()
                .build();
    }
}
