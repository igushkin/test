package ie.ncirl.esta.controller;

import ie.ncirl.esta.dto.CarerDto;
import ie.ncirl.esta.dto.MessageTemplateDto;
import ie.ncirl.esta.security.UserPrincipal;
import ie.ncirl.esta.service.TherapistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/therapists")
public class TherapistController {

    private final TherapistService therapistService;

    @PostMapping("/carers")
    public ResponseEntity<CarerDto> createCarer(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CarerDto dto) {
        return new ResponseEntity<>(
                therapistService.createCarer(((UserPrincipal) userDetails).getId(), dto),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/message-templates")
    public ResponseEntity<CarerDto> createMessageTemplate(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MessageTemplateDto dto) {
        return new ResponseEntity<>(
                therapistService.createCarer(((UserPrincipal) userDetails).getId(), dto),
                HttpStatus.CREATED
        );
    }


}