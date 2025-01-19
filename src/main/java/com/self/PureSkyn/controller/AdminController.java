package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.Booking;
import com.self.PureSkyn.Model.Technician;
import com.self.PureSkyn.exception.BadRequestException;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.service.BookingService;
import com.self.PureSkyn.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TechnicianService technicianService;

    @PutMapping("/{bookingId}/assignTechnician")
    public ResponseEntity<?> assignTechnician(@PathVariable int bookingId,
                                              @RequestParam int technicianId) {
        try {
            Booking updatedBooking = bookingService.assignTechnician(bookingId, technicianId);
            return ResponseEntity.ok(updatedBooking);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/available-technicians")
    public ResponseEntity<?> getAvailableTechnicians(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("timeSlot") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeSlot) {

        List<Technician> availableTechnicians = technicianService.getAvailableTechnicians(serviceId, date, timeSlot);

        if (availableTechnicians.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No technicians available");
        }
        return ResponseEntity.ok(availableTechnicians);
    }

}
