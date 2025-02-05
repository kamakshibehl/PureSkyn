package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.LoginRequestDTO;
import com.self.PureSkyn.Model.User;
import com.self.PureSkyn.Model.UserLoginDTO;
import com.self.PureSkyn.Model.UserSignUpDTO;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.UserRepo;
import com.self.PureSkyn.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @Value("${app.domain.url}")
    private String domainUrl;

    public UserLoginDTO registerUser(UserSignUpDTO userSignUpDTO) {

        String normalizedEmail = userSignUpDTO.getEmail().toLowerCase();

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword()));
        user.setFirstName(userSignUpDTO.getFirstName());
        user.setLastName(userSignUpDTO.getLastName());
        user.setPhone(userSignUpDTO.getPhone());

        userRepo.save(user);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());
        String jwt = jwtUtils.generateToken(userDetails);

        return new UserLoginDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                jwt
        );
    }

    public UserLoginDTO authenticateUser(LoginRequestDTO loginRequest) {
        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());
        String jwt = jwtUtils.generateToken(userDetails);

        return new UserLoginDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                user.getPhone(),
                jwt
        );
    }

    public String requestPasswordChange(String email) {

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
//        if (userDetails == null) {
//            throw new UsernameNotFoundException("User not found.");
//        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("purpose", "password-reset");
        String token = jwtUtils.generateToken(claims, userDetails);

        String passwordChangeLink = domainUrl + "/change-password?token=" + token;

        emailService.sendEmail(
                email,
                "Password Change Request",
                "Hello,\n\nClick the link below to change your password:\n\n" + passwordChangeLink + "\n\nIf you did not request this, please ignore this email."
        );

        return "Password change link sent to your email.";
    }

    public String changePassword(String token, String oldPassword, String newPassword) {
        String email = jwtUtils.extractUsername(token);

        if (!jwtUtils.validateToken(token, email)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        return "Password changed successfully.";
    }
}

