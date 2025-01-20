package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.Address;
import com.self.PureSkyn.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/{userId}")
    public List<Address> getAddressesByUserId(@PathVariable String userId) {
        return addressService.getAddressesByUserId(userId);
    }
}
