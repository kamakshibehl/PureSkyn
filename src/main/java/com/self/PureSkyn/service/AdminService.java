package com.self.PureSkyn.service;

import com.mongodb.annotations.Alpha;
import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.AdminRepo;
import com.self.PureSkyn.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
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

    public AdminLoginDTO registerAdmin(AdminSignUpDTO admin) {
        String normalizedEmail = admin.getEmail().toLowerCase();

        Admin newAdmin = new Admin();
        newAdmin.setEmail(normalizedEmail);
        newAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        newAdmin.setName(admin.getName());
        newAdmin.setPhone(admin.getPhone());
        newAdmin.setRole("ROLE_ADMIN");

        newAdmin = adminRepo.save(newAdmin);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(admin.getEmail());
        String jwt = jwtUtils.generateToken(userDetails);

        return new AdminLoginDTO(
                newAdmin.getId(),
                newAdmin.getEmail(),
                newAdmin.getName(),
                newAdmin.getPhone(),
                newAdmin.getRole(),
                jwt
        );
    }

    public AdminLoginDTO authenticateUser(LoginRequestDTO loginRequest) {
        Admin user = adminRepo.findByEmail(loginRequest.getEmail().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());
        String jwt = jwtUtils.generateToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        return new AdminLoginDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                role,
                jwt
        );
    }

}
