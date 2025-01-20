package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.User;
import com.self.PureSkyn.Model.UserLoginDTO;
import com.self.PureSkyn.Model.UserSignUpDTO;
import com.self.PureSkyn.Model.UserUpdateDTO;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.UserRepo;
import com.self.PureSkyn.security.JwtUtils;
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

    @Autowired
    private UserService userService;

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
    private PasswordEncoder passwordEncoder;

    @Value("${app.domain.url}")
    private String domainUrl;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserSignUpDTO userDTO) {
        userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String email, @RequestParam String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            String jwt = jwtUtils.generateToken(userDetails);

            UserLoginDTO response = new UserLoginDTO(user.getId(), user.getEmail(), jwt);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/request-password-change")
    public ResponseEntity<?> requestPasswordChange(@RequestParam String email) {
        try {

            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
            if (userDetails == null) {
                return ResponseEntity.status(404).body("User not found.");
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("purpose", "password-reset");
            String token = jwtUtils.generateToken(claims, userDetails);

            String passwordChangeLink = domainUrl + "/change-password?token=" + token;

            emailService.sendEmail(
                    email,
                    "Password Change Request",
                    "Hello,\n\nClick the link below to change your password:\n\n" + passwordChangeLink + "\n\nIf you did not request this, please ignore this email."
            );

            return ResponseEntity.ok("Password change link sent to your email.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while processing the request.");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String token,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        try {

            String email = jwtUtils.extractUsername(token);

            if (!jwtUtils.validateToken(token, email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
            }

            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);

            return ResponseEntity.ok("Password changed successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing the password.");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody UserUpdateDTO updateRequest) {
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
