package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Role;
import com.self.PureSkyn.Model.User;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.UserRepo;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Data
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

//    public UserService() {
//
//    }

    public User registerUser(String username, String email, String rawPassword, boolean isAdmin) {
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        if (isAdmin) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        return user.orElseThrow(() -> new ResourceNotFoundException("Username not found: " + username));
    }
}
