package k.wakir.dbcbackend.controller.authentication;

import k.wakir.dbcbackend.controller.authentication.dto.AuthRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        return "Login";
    }
}
