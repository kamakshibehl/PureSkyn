package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.UserRepo;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final BookingService bookingService;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, @Lazy BookingService bookingService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.bookingService = bookingService;
    }


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepo.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("Username not found: " + username));
//    }

    public List<UserDetailsDTO> getAllUserDetails() {
        List<User> users = userRepo.findAll();

        return users.stream().map(this::convertToUserDetailsDTO).collect(Collectors.toList());
    }

    public UserUpdateDTO updateUserProfile(String id, UserUpdateDTO updateRequest) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

//        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
//            if (userRepo.existsByEmail(updateRequest.getEmail())) {
//                throw new IllegalArgumentException("Email already exists");
//            }
//            user.setEmail(updateRequest.getEmail());
//        }
//
//        if (updateRequest.getPhone() != null && !updateRequest.getPhone().equals(user.getPhone())) {
//            if (userRepo.existsByPhone(updateRequest.getPhone())) {
//                throw new IllegalArgumentException("Phone number already exists");
//            }
//            user.setPhone(updateRequest.getPhone());
//        }

        if (StringUtils.hasText(updateRequest.getFirstName())) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (StringUtils.hasText(updateRequest.getLastName())) {
            user.setLastName(updateRequest.getLastName());
        }
        if (StringUtils.hasText(updateRequest.getGender())) {
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

    private UserDetailsDTO convertToUserDetailsDTO(User user) {

        List<BookingDTO> bookings = bookingService.getBookingsByUserId(user.getId());

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setId(user.getId());
        userDetailsDTO.setFirstName(user.getFirstName());
        userDetailsDTO.setLastName(user.getLastName());
        userDetailsDTO.setEmail(user.getEmail());
        userDetailsDTO.setPhone(user.getPhone());
        userDetailsDTO.setGender(user.getGender());
        userDetailsDTO.setAddresses(user.getAddresses());
        userDetailsDTO.setBookings(bookings);

        return userDetailsDTO;
    }
}
