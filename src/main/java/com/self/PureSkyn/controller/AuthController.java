package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.UserRepo;
import com.self.PureSkyn.security.JwtUtils;
import com.self.PureSkyn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.domain.url}")
    private String domainUrl;

    private final UserService userService;

    private final AddressService addressService;

    public AuthController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserLoginDTO>> registerUser(@RequestBody UserSignUpDTO userDTO) {
        String normalizedEmail = userDTO.getEmail().toLowerCase();

        if (userRepo.existsByEmail(normalizedEmail)) {
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.ERROR, "Email already exists", null));
        }

        if (userRepo.existsByPhone(userDTO.getPhone())) {
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.ERROR, "Contact Number already exists", null));
        }

        try {
            UserLoginDTO userLoginDTO = authService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(ApiResponseStatus.SUCCESS, "User registered successfully", userLoginDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginDTO>> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            UserLoginDTO userLoginDTO = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.SUCCESS,
                    "Login successful",
                    userLoginDTO
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    "Invalid credentials",
                    null
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }


    @PostMapping("/request-password-change")
    public ResponseEntity<ApiResponse<?>> requestPasswordChange(@RequestParam String email) {
        try {
            String responseMessage = authService.requestPasswordChange(email);
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, responseMessage));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.ERROR, "User Not Found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while processing the request"));
        }
    }


    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            String responseMessage = authService.changePassword(
                    changePasswordDTO.getToken(),
                    changePasswordDTO.getOldPassword(),
                    changePasswordDTO.getNewPassword()
            );

            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, responseMessage));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage(),
                    null
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while changing the password"));
        }
    }

    @PutMapping("/update-user")
    public ResponseEntity<ApiResponse<UserUpdateDTO>> updateUser(@RequestBody UserUpdateDTO request) {
        try {
            UserUpdateDTO updatedUser = userService.updateUserProfile(request.getId(), request);
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "User updated successfully", updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage(),
                    null
            ));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.ERROR, "User Not Found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }

    @PutMapping("/update-address")
    public ResponseEntity<ApiResponse<UserUpdateDTO>> updateAddress(@RequestBody AddressUpdateRequestDTO request) {
        try {
            UserUpdateDTO updatedUser = userService.updateAddress(request);
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Address updated successfully", updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, e.getMessage()));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.ERROR, "User Not Found", null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while updating address"));
        }
    }

    @PostMapping("/user/add-address")
    public ResponseEntity<ApiResponse<UserDetailsDTO>> addAddress(@RequestBody AddAddressRequestDTO request) {
        try {
            UserDetailsDTO updatedUser = addressService.addAddressToUser(request.getUserId(), request.getAddress());
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Address added successfully", updatedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }

    @DeleteMapping("/user/delete-address")
    public ResponseEntity<ApiResponse<UserDetailsDTO>> deleteAddress( @RequestParam String userId,
                                                                      @RequestParam String addressId) {
        try {
            UserDetailsDTO updatedUser = addressService.deleteAddressFromUser(userId, addressId);
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Address added successfully", updatedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }

}
