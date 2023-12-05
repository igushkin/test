package ie.ncirl.esta.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MessageTemplateDto {
    private Long id;
    private String name;
    private String subject;
    private String text;
}
