package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.BadRequestException;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.AdminRepo;
import com.self.PureSkyn.service.AdminService;
import com.self.PureSkyn.service.BookingService;
import com.self.PureSkyn.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = "https://milanmishra1206.github.io/")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private AdminService adminService;

    @PutMapping("/{bookingId}/assignTechnician")
    @PreAuthorize("@adminService.isCurrentUserAdmin()")
    public ResponseEntity<?> assignTechnician(@PathVariable String bookingId,
                                              @RequestParam String technicianId) {
        try {
            BookingDTO updatedBooking = bookingService.assignTechnician(bookingId, technicianId);
            return ResponseEntity.ok(updatedBooking);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/available-technicians")
    @PreAuthorize("@adminService.isCurrentUserAdmin()")
    public ResponseEntity<?> getAvailableTechnicians(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("timeSlot") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeSlot) {

        List<TechnicianDTO> availableTechnicians = technicianService.getAvailableTechnicians(serviceId, date, timeSlot);

        if (availableTechnicians.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No technicians available");
        }
        return ResponseEntity.ok(availableTechnicians);
    }

//    @PostMapping("/register")
//    public ResponseEntity<ApiResponse<UserLoginDTO>> registerUser(@RequestBody UserSignUpDTO userDTO) {
//        String normalizedEmail = userDTO.getEmail().toLowerCase();
//
//        if (adminRepo.existsByEmail(normalizedEmail)) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "Email already exists"));
//        }
//
//        if (adminRepo.existsByPhone(userDTO.getPhone())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "Contact number already exists"));
//        }
//
//        try {
//            UserLoginDTO userLoginDTO = adminService.registerUser(userDTO);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(new ApiResponse<>(ApiResponseStatus.SUCCESS, "User registered successfully", userLoginDTO));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
//        }
//    }



//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<UserLoginDTO>> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
//        try {
//            UserLoginDTO userLoginDTO = adminService.authenticateUser(loginRequest);
//            return ResponseEntity.ok(new ApiResponse<>(
//                    ApiResponseStatus.SUCCESS,
//                    "Login successful",
//                    userLoginDTO
//            ));
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, "Invalid credentials"));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
//        }
//    }
//
//
//    @PostMapping("/request-password-change")
//    public ResponseEntity<ApiResponse<?>> requestPasswordChange(@RequestParam String email) {
//        try {
//            String responseMessage = adminService.requestPasswordChange(email);
//            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, responseMessage));
//        } catch (UsernameNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "User not found"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while processing the request"));
//        }
//    }
//
//
//    @PostMapping("/change-password")
//    public ResponseEntity<ApiResponse<?>> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
//        try {
//            String responseMessage = adminService.changePassword(
//                    changePasswordDTO.getToken(),
//                    changePasswordDTO.getOldPassword(),
//                    changePasswordDTO.getNewPassword()
//            );
//
//            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, responseMessage));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, e.getMessage()));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while changing the password"));
//        }
//    }

}
