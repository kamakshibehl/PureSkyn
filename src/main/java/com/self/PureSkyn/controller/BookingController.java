package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.BookingDTO;
import com.self.PureSkyn.Model.PriceDetailsDTO;
import com.self.PureSkyn.exception.BadRequestException;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.self.PureSkyn.Model.Booking;
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
    public ResponseEntity<List<BookingDTO>> getAllBookingsInDescOrder() {
        List<BookingDTO> bookings = bookingService.getAllBookingsInDescOrder();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByUserId(@PathVariable String userId) {
        List<BookingDTO> userBookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(userBookings);
    }

    @PostMapping("/request")
    public ResponseEntity<PriceDetailsDTO> requestBooking(
            @RequestParam String serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeSlot) {
        try {
            PriceDetailsDTO priceDetails = bookingService.requestBooking(serviceId, date, timeSlot);
            return ResponseEntity.ok(priceDetails);
        } catch (BadRequestException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        String responseMessage = "Booking created successfully with ID: " + createdBooking.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @GetMapping("/pending")
    @PreAuthorize("@adminService.isCurrentUserAdmin()")
    public ResponseEntity<List<BookingDTO>> getAllPendingBookings() {
        List<BookingDTO> pendingBookingDTOs = bookingService.getAllPendingBookings();
        return ResponseEntity.ok(pendingBookingDTOs);
    }

    @GetMapping("/by-date")
    @PreAuthorize("@adminService.isCurrentUserAdmin()")
    public ResponseEntity<List<BookingDTO>> getBookingsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BookingDTO> bookingDTOs = bookingService.getBookingsByDate(date);
        return ResponseEntity.ok(bookingDTOs);
    }

    @PatchMapping("/{bookingId}/complete")
    public ResponseEntity<?> markBookingAsCompleted(@PathVariable String bookingId) {
        try {
            Booking updatedBooking = bookingService.markBookingAsCompleted(bookingId);
            return ResponseEntity.ok("Booking marked as completed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<?> markBookingAsCanceled(@PathVariable String bookingId) {
        try {
            Booking updatedBooking = bookingService.markBookingAsCancelled(bookingId);
            return ResponseEntity.ok("Booking marked as cancelled successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

}

