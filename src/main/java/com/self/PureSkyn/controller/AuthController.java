package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.UserRepo;
import com.self.PureSkyn.security.JwtUtils;
import com.self.PureSkyn.service.AuthService;
import com.self.PureSkyn.service.EmailService;
import com.self.PureSkyn.service.UserDetailsServiceImpl;
import com.self.PureSkyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

//    @Autowired
//    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @Autowired
    AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.domain.url}")
    private String domainUrl;

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserLoginDTO> registerUser(@RequestBody UserSignUpDTO userDTO) {
        UserLoginDTO userLoginDTO = authService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userLoginDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            UserLoginDTO userLoginDTO = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(userLoginDTO);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/request-password-change")
    public ResponseEntity<?> requestPasswordChange(@RequestParam String email) {
        try {
            String responseMessage = authService.requestPasswordChange(email);
            return ResponseEntity.ok(responseMessage);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String token,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        try {
            String responseMessage = authService.changePassword(token, oldPassword, newPassword);
            return ResponseEntity.ok(responseMessage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing the password.");
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserUpdateDTO updateRequest) {
        try {
            UserUpdateDTO updatedUser = userService.updateUserProfile(id, updateRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

}
