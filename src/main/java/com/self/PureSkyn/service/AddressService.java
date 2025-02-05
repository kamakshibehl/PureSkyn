package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Address;
import com.self.PureSkyn.Model.User;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.AddressRepo;
import com.self.PureSkyn.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Address> getAddressesByUserId(String userId) {
        return addressRepo.findByUserId(userId);
    }
    public User addAddressToUser(String userId, Address newAddress) {
        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        User user = optionalUser.get();

        if (user.getAddresses() == null) {
            user.setAddresses(new ArrayList<>());
        }

        user.getAddresses().add(newAddress);

        return userRepo.save(user);
    }

}
