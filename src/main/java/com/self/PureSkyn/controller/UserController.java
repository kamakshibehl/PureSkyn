package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.Address;
import com.self.PureSkyn.Model.User;
import com.self.PureSkyn.service.AddressService;
import com.self.PureSkyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/all")
    @PreAuthorize("@adminService.isCurrentUserAdmin()")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
