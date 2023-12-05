package ie.ncirl.esta.service;

import ie.ncirl.esta.dto.MessageTemplateDto;

public interface MessageTemplateService {

    MessageTemplateDto createMessageTemplate(Long userId, MessageTemplateDto messageTemplateDto);

    MessageTemplateDto updateMessageTemplate(Long userId, MessageTemplateDto messageTemplateDto);

    MessageTemplateDto deleteMessageTemplate(Long userId, Long messageTemplateId);

    MessageTemplateDto duplicateMessageTemplate(Long userId, Long messageTemplateId);
}
