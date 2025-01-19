package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.User;
import com.self.PureSkyn.Model.UserLoginDTO;
import com.self.PureSkyn.Model.UserSignUpDTO;
import com.self.PureSkyn.exception.BadRequestException;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.UserRepo;
import com.self.PureSkyn.security.JwtUtils;
import com.self.PureSkyn.security.SecurityConfig;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String email, @RequestParam String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);

            String jwt = jwtUtils.generateToken(userDetails);

            UserLoginDTO response = new UserLoginDTO(userDetails.getUsername(), jwt);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
