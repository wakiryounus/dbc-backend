package k.wakir.dbcbackend.controller.authentication;

import jakarta.validation.Valid;
import k.wakir.dbcbackend.controller.authentication.dto.LoginRequest;
import k.wakir.dbcbackend.controller.authentication.dto.RegisterRequest;
import k.wakir.dbcbackend.model.ServerMessage;
import k.wakir.dbcbackend.model.entity.User;
import k.wakir.dbcbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ServerMessage> register(@RequestBody RegisterRequest registerRequest) {
        User user = new User(null, registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
        this.userService.saveUser(user);
        return new ResponseEntity<>(new ServerMessage("User Registered Successfully"), HttpStatus.CREATED);
    }
}
