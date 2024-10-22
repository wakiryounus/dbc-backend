package k.wakir.dbcbackend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import k.wakir.dbcbackend.model.entity.User;
import k.wakir.dbcbackend.repository.UserRepository;
import k.wakir.dbcbackend.security.jwt.JwtService;
import k.wakir.dbcbackend.utils.PasswordUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void saveUser(User user) {
        if (this.userRepository.findByUsername(user.getUsername()).isPresent())
            throw new RuntimeException("Username already taken");
        user.setPassword(PasswordUtil.encryptPassword(user.getPassword()));
        this.userRepository.save(user);
    }

    public String loginUser(String username, String password) {
        try {
            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
            // Generate the JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Return the JWT in the response
            return this.jwtService.generateToken(userDetails.getUsername());

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public User loadUserFromToken(String token) {
        String username = "";
        try {
            username = this.jwtService.extractUsername(token);
        } catch (Exception exception) {
            return null;
        }
        return this.userRepository.findByUsername(username)
                .orElseThrow(() ->  new RuntimeException("No user found"));
    }

    public void logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
