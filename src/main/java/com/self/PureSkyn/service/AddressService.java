package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.AddressRepo;
import com.self.PureSkyn.repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BookingService bookingService;

    public List<Address> getAddressesByUserId(String userId) {
        Optional<User> userOptional = userRepo.findById(userId);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        User user = userOptional.get();
        List<Address> addresses = user.getAddresses();

        if (addresses == null || addresses.isEmpty()) {
            throw new ResourceNotFoundException("No addresses found for user with ID: " + userId);
        }

        return addresses;
    }


    public UserDetailsDTO addAddressToUser(String userId, Address newAddress) {
        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        User user = optionalUser.get();

        newAddress.setId(new ObjectId().toString());

        if (user.getAddresses() == null) {
            user.setAddresses(new ArrayList<>());
        }

        user.getAddresses().add(newAddress);

        return convertToUserDetailsDTO(userRepo.save(user));
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
