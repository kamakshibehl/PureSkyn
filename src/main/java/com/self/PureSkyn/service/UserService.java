package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Address;
import com.self.PureSkyn.Model.Role;
import com.self.PureSkyn.Model.User;
import com.self.PureSkyn.Model.UserSignUpDTO;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.UserRepo;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserSignUpDTO userSignUpDTO, boolean isAdmin) {
        if (userRepo.existsByEmail(userSignUpDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(userSignUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword()));

        if (isAdmin) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Username not found: " + username));
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
