package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.service.AddressService;
import com.self.PureSkyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<List<UserDetailsDTO>>> getAllUsers() {
        try {
            List<UserDetailsDTO> users = userService.getAllUserDetails();
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new ApiResponse<>(ApiResponseStatus.SUCCESS, "No users found", users)
                );
            }

            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Users retrieved successfully", users));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred")
            );
        }
    }

}
