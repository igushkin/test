package ie.ncirl.esta.controller;

import ie.ncirl.esta.dto.UserDto;
import ie.ncirl.esta.service.UserService;
import ie.ncirl.esta.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final VerificationService verificationService;

    @GetMapping("/init")
    public String init() {
        userService.init();
        return "ok";
    }

/*    @GetMapping
    public String approveAccount(Model model, @RequestParam Integer verificationCode) {
        model.addAttribute("code", verificationCode);
        return "conf";
    }*/

    @PostMapping("/{verificationCode}")
    public UserDto verifyAccount(@RequestBody UserDto dto, @PathVariable Integer verificationCode) {
        return verificationService.verifyAccount(verificationCode, dto.getPassword());
    }
}
