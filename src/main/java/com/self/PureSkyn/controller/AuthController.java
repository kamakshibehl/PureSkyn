package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.User;
import com.self.PureSkyn.Model.UserLoginDTO;
import com.self.PureSkyn.Model.UserSignUpDTO;
import com.self.PureSkyn.security.JwtUtils;
import com.self.PureSkyn.service.UserDetailsServiceImpl;
import com.self.PureSkyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestParam UserSignUpDTO userSignUpDTO) {
        User newUser = userService.registerUser(userSignUpDTO, false);
        return ResponseEntity.ok("User registered successfully: " + newUser.getUsername());
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestParam UserSignUpDTO userSignUpDTO) {
        User newAdmin = userService.registerUser(userSignUpDTO, true);
        return ResponseEntity.ok("Admin registered successfully: " + newAdmin.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String email,
                                              @RequestParam String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
        String jwt = jwtUtils.generateToken(email, password);

        UserLoginDTO response = new UserLoginDTO(userDetails.getUsername(), jwt);

        return ResponseEntity.ok(response);
    }

}
