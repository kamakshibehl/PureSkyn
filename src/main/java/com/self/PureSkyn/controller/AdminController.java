package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.Booking;
import com.self.PureSkyn.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/pending")
    public List<Booking> getPendingBookings() {
        return bookingService.getPendingBookings();
    }

    @PutMapping("/{bookingId}/assignTechnician")
    public Booking assignTechnician(@PathVariable int bookingId,
                                    @RequestParam int technicianId) {
        return bookingService.assignTechnician(bookingId, technicianId);
    }
}
