package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.UserRepo;
import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserSignUpDTO userSignUpDTO) {
        if (userRepo.existsByEmail(userSignUpDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(userSignUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword()));

        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Username not found: " + username));
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public UserUpdateDTO updateUserProfile(int id, UserUpdateDTO updateRequest) {
            User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

            if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
                if (userRepo.existsByEmail(updateRequest.getEmail())) {
                    throw new IllegalArgumentException("Email already exists");
                }
                user.setEmail(updateRequest.getEmail());
            }

            if (updateRequest.getPhone() != null && !updateRequest.getPhone().equals(user.getPhone())) {
                if (userRepo.existsByPhone(updateRequest.getPhone())) {
                    throw new IllegalArgumentException("Phone number already exists");
                }
                user.setPhone(updateRequest.getPhone());
            }

            if (updateRequest.getFirstName() != null) {
                user.setFirstName(updateRequest.getFirstName());
            }
            if (updateRequest.getLastName() != null) {
                user.setLastName(updateRequest.getLastName());
            }
            if (updateRequest.getGender() != null) {
                user.setGender(updateRequest.getGender());
            }

            if (updateRequest.getAddresses() != null && !updateRequest.getAddresses().isEmpty()) {
                if (user.getAddresses() == null) {
                    user.setAddresses(new ArrayList<>());
                }
                user.getAddresses().addAll(updateRequest.getAddresses());
            }

        User updatedUser = userRepo.save(user);

        return convertToUserUpdateDTO(updatedUser);
        }


    private UserUpdateDTO convertToUserUpdateDTO(User user) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setAddresses(user.getAddresses());
        return dto;
    }


}
