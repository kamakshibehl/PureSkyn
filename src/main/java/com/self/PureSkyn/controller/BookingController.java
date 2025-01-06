package com.self.PureSkyn.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.self.PureSkyn.Model.Booking;
import com.self.PureSkyn.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        String responseMessage = "Booking created successfully with ID: " + createdBooking.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }
}

