package com.self.PureSkyn.service;

import com.mongodb.annotations.Alpha;
import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.AdminRepo;
import com.self.PureSkyn.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("adminService")
public class AdminService {
    private final AdminRepo adminRepo;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = ((User) authentication.getPrincipal()).getEmail();
        return adminRepo.existsByEmail(currentUserEmail);
    }


//    public UserLoginDTO registerUser(UserSignUpDTO userSignUpDTO) {
//
//        String normalizedEmail = userSignUpDTO.getEmail().toLowerCase();
//
//        Admin user = new Admin();
//        user.setEmail(normalizedEmail);
//        user.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword()));
//        user.setFirstName(userSignUpDTO.getFirstName());
//        user.setLastName(userSignUpDTO.getLastName());
//        user.setPhone(userSignUpDTO.getPhone());
//        user.setAdmin(true);
//
//        adminRepo.save(user);
//
//        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());
//        String jwt = jwtUtils.generateToken(userDetails);
//
//        return new UserLoginDTO(
//                user.getId(),
//                user.getEmail(),
//                user.getName(),
//                user.getPhone(),
//                jwt
//        );
//    }

//    public UserLoginDTO authenticateUser(LoginRequestDTO loginRequest) {
//        User user = adminRepo.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new BadCredentialsException("Invalid credentials");
//        }
//
//        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());
//        String jwt = jwtUtils.generateToken(userDetails);
//
//        return new UserLoginDTO(
//                user.getId(),
//                user.getEmail(),
//                user.getFirstName() + " " + user.getLastName(),
//                user.getPhone(),
//                jwt
//        );
//    }
}
