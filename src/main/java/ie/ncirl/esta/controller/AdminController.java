package ie.ncirl.esta.controller;

import ie.ncirl.esta.dto.TherapistDto;
import ie.ncirl.esta.service.AdminService;
import ie.ncirl.esta.service.TherapistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final TherapistService therapistService;
    private final AdminService adminService;

    @GetMapping("/init")
    public void init() {
        adminService.init();
    }

    @GetMapping("/therapists")
    public ResponseEntity<List<TherapistDto>> findAllTherapists() {
        return new ResponseEntity<>(therapistService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/therapists")
    public ResponseEntity<TherapistDto> createTherapist(@RequestBody TherapistDto dto) {
        return new ResponseEntity<>(adminService.createTherapist(dto), HttpStatus.OK);
    }
}
