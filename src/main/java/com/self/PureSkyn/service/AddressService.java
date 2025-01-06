package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Address;
import com.self.PureSkyn.repository.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepo addressRepo;

    public List<Address> getAddressesByUserId(Integer userId) {
        return addressRepo.findByUserId(userId);
    }
}
