package ie.ncirl.esta.controller;

import ie.ncirl.esta.dto.ChildDto;
import ie.ncirl.esta.security.UserPrincipal;
import ie.ncirl.esta.service.CarerService;
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
@RequestMapping("/carers")
public class CarerController {

    private final CarerService carerService;

    @PostMapping("/children")
    public ResponseEntity<ChildDto> createChild(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ChildDto dto) {
        return new ResponseEntity<>(carerService.createChild(((UserPrincipal) userDetails).getId(), dto), HttpStatus.CREATED);
    }
}
