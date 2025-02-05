package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.BadRequestException;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.self.PureSkyn.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/all")
    @PreAuthorize("@adminService.isCurrentUserAdmin()")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getAllBookingsInDescOrder() {
        try {
            List<BookingDTO> bookings = bookingService.getAllBookingsInDescOrder();

            if (bookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new ApiResponse<>(ApiResponseStatus.SUCCESS, "No bookings found", bookings)
                );
            }

            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Bookings retrieved successfully", bookings));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred")
            );
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getBookingsByUserId(@PathVariable String userId) {
        try {
            List<BookingDTO> userBookings = bookingService.getBookingsByUserId(userId);

            if (userBookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new ApiResponse<>(ApiResponseStatus.SUCCESS, "No bookings found for the user", userBookings)
                );
            }

            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "User bookings retrieved successfully", userBookings));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(ApiResponseStatus.FAIL, "User not found")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred")
            );
        }
    }


    @PostMapping("/request")
    public ResponseEntity<ApiResponse<BookingRequestResponseDTO>> requestBooking(@RequestBody BookingRequestDTO bookingRequest) {
        try {
            BookingRequestResponseDTO bookingDetails = bookingService.requestBooking(
                    bookingRequest.getServiceId(),
                    bookingRequest.getSubServiceId(),
                    bookingRequest.getDate()
            );

            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.SUCCESS,
                    "Booking request processed successfully",
                    bookingDetails
            ));

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage()
            ));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    e.getMessage()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    ApiResponseStatus.ERROR,
                    "An unexpected error occurred"
            ));
        }
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(@RequestBody Booking bookingRequest) {
        try {
            BookingDTO createdBooking = bookingService.createBooking(bookingRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Booking created successfully", createdBooking));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }


    @GetMapping("/pending")
    @PreAuthorize("@adminService.isCurrentUserAdmin()")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getAllPendingBookings() {
        try {
            List<BookingDTO> pendingBookingDTOs = bookingService.getAllPendingBookings();

            if (pendingBookingDTOs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse<>(ApiResponseStatus.SUCCESS, "No pending bookings found", pendingBookingDTOs));
            }
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Pending bookings retrieved successfully", pendingBookingDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }


    @GetMapping("/by-date")
    @PreAuthorize("@adminService.isCurrentUserAdmin()")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getBookingsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<BookingDTO> bookingDTOs = bookingService.getBookingsByDate(date);

            if (bookingDTOs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse<>(ApiResponseStatus.SUCCESS, "No bookings found for the specified date", bookingDTOs));
            }

            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Bookings retrieved successfully", bookingDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred"));
        }
    }


    @PatchMapping("/{bookingId}/complete")
    public ResponseEntity<ApiResponse<String>> markBookingAsCompleted(@PathVariable String bookingId) {
        try {
            Booking updatedBooking = bookingService.markBookingAsCompleted(bookingId);

            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Booking marked as completed successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred"));
        }
    }


    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<String>> markBookingAsCanceled(@PathVariable String bookingId) {
        try {
            Booking updatedBooking = bookingService.markBookingAsCancelled(bookingId);

            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Booking marked as cancelled successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ApiResponseStatus.FAIL, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred"));
        }
    }

}

