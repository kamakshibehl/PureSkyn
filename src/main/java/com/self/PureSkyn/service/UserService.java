package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.Model.request.AddressUpdateRequestDTO;
import com.self.PureSkyn.Model.response.BookingDTO;
import com.self.PureSkyn.repository.UserRepo;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    public List<UserDetailsDTO> getAllUserDetails() {
        List<User> users = userRepo.findAll();

        return users.stream().map(this::convertToUserDetailsDTO).collect(Collectors.toList());
    }

    public UserUpdateDTO updateUserProfile(String id, UserUpdateDTO updateRequest) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        if (StringUtils.hasText(updateRequest.getName())) {
            user.setName(updateRequest.getName());
        }

        if (StringUtils.hasText(updateRequest.getPhone())) {
            boolean phoneExists = userRepo.existsByPhone(updateRequest.getPhone());
            if (phoneExists && !user.getPhone().equals(updateRequest.getPhone())) {
                throw new IllegalArgumentException("Phone number already exists");
            }
            user.setPhone(updateRequest.getPhone());
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

    public UserUpdateDTO updateAddress(AddressUpdateRequestDTO request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Address> addresses = user.getAddresses();

        boolean addressFound = false;
        for (int i = 0; i < addresses.size(); i++) {
            if (addresses.get(i).getId().equals(request.getAddressId())) {
                addresses.set(i, request.getUpdatedAddress());
                addressFound = true;
                break;
            }
        }

        if (!addressFound) {
            throw new IllegalArgumentException("Address not found");
        }

        user.setAddresses(addresses);
        User updatedUser = userRepo.save(user);
        return convertToUserUpdateDTO(updatedUser);
    }


    private UserUpdateDTO convertToUserUpdateDTO(User user) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
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
        userDetailsDTO.setName(user.getName());
        userDetailsDTO.setEmail(user.getEmail());
        userDetailsDTO.setPhone(user.getPhone());
        userDetailsDTO.setGender(user.getGender());
        userDetailsDTO.setAddresses(user.getAddresses());
        userDetailsDTO.setBookings(bookings);

        return userDetailsDTO;
    }
}
