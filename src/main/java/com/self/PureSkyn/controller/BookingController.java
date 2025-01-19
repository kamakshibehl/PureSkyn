package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.BookingDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.self.PureSkyn.Model.Booking;
import com.self.PureSkyn.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingDTO>> getAllBookingsInDescOrder() {
        List<BookingDTO> bookings = bookingService.getAllBookingsInDescOrder();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByUserId(@PathVariable String userId) {
        List<BookingDTO> userBookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(userBookings);
    }


    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        String responseMessage = "Booking created successfully with ID: " + createdBooking.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingDTO>> getAllPendingBookings() {
        // Fetch BookingDTOs from the service
        List<BookingDTO> pendingBookingDTOs = bookingService.getAllPendingBookings();
        return ResponseEntity.ok(pendingBookingDTOs);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<BookingDTO>> getBookingsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BookingDTO> bookingDTOs = bookingService.getBookingsByDate(date);
        return ResponseEntity.ok(bookingDTOs);
    }



}

