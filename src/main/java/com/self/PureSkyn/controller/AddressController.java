package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.Address;
import com.self.PureSkyn.Model.ApiResponse;
import com.self.PureSkyn.Model.ApiResponseStatus;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Address>>> getAddressesByUserId(@PathVariable String userId) {
        try {
            List<Address> addresses = addressService.getAddressesByUserId(userId);
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Addresses retrieved successfully", addresses));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }

}
