package k.wakir.dbcbackend.controller.authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<ServerMessage> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String jwtToken = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        Cookie cookie = new Cookie("authToken", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        cookie.setSecure(true);
//        Setting this to false means the cookie can be sent over both HTTP and HTTPS.
//        If it were set to true, the cookie would only be sent over secure HTTPS connections
        response.addCookie(cookie);
        return new ResponseEntity<>(new ServerMessage("Login Successfully", null),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ServerMessage> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User(null, registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
        this.userService.saveUser(user);
        return new ResponseEntity<>(new ServerMessage("User Registered Successfully", null), HttpStatus.CREATED);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<ServerMessage> currentUser(@CookieValue(value = "authToken") String token) {
        return new ResponseEntity<>(new ServerMessage("Loaded successfully",
                this.userService.loadUserFromToken(token)), HttpStatus.OK);
    }
}
