package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Booking;
import com.self.PureSkyn.Model.LaserService;
import com.self.PureSkyn.Model.Technician;
import com.self.PureSkyn.exception.BadRequestException;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.BookingRepo;
import com.self.PureSkyn.repository.ServiceRepo;
import com.self.PureSkyn.repository.TechnicianAvailablityRepo;
import com.self.PureSkyn.repository.TechnicianRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

//@Service
//public class BookingService {
//
//    @Autowired
//    private BookingRepo bookingRepo;
//
//    public Booking createBooking(Booking booking) {
//        return bookingRepo.save(booking);
//    }
//
//    public List<Booking> getPendingBookings() {
//        return bookingRepo.getPendingBookings();
//    }
//
//    public Booking assignTechnician(int bookingId, int technicianId) {
//        Booking booking = bookingRepo.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//        booking.setTechnicianId(technicianId);
//        booking.setAssigned(true);
//        return bookingRepo.save(booking);
//    }
//
//    public List<Booking> getBookingsForDate(LocalDate date) {
//        return bookingRepo.findByDate(date);
//    }
//}


@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private TechnicianRepo technicianRepo;

    @Autowired
    private TechnicianService technicianService;

    public Booking createBooking(Booking booking) {
        LocalDate today = LocalDate.now();
        if (booking.getDate() == null || booking.getTimeSlot() == null) {
            throw new BadRequestException("Date and timeSlot must be specified");
        }
        if (booking.getDate().isBefore(today)) {
            throw new BadRequestException("Cannot book a date in the past");
        }

        LaserService laserService = serviceRepo.findById(booking.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        boolean validForService = false;
        for (String pin : laserService.getValidPinCodes()) {
            if (pin.equals(booking.getPinCode())) {
                validForService = true;
                break;
            }
        }
        if (!validForService) {
            throw new BadRequestException("Service is not available in pin code " + booking.getPinCode());
        }

//        if (booking.getTechnicianId() != null && !booking.getTechnicianId().isBlank()) {
//            Technician technician = technicianRepo.findById(booking.getTechnicianId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found"));
//
//            if (technician.getAvailablePinCodes() == null ||
//                    !technician.getAvailablePinCodes().contains(booking.getPinCode())) {
//                throw new BadRequestException(
//                        "Technician does not serve pin code " + booking.getPinCode()
//                );
//            }
//
//            boolean isAvailable = technicianAvailabilityService.isTechnicianAvailable(
//                    technician.getId(),
//                    booking.getDate(),
//                    booking.getTimeSlot()
//            );
//            if (!isAvailable) {
//                throw new BadRequestException("Technician is not available for that date/time");
//            }
//            technicianAvailabilityService.bookSlot(
//                    technician.getId(),
//                    booking.getDate(),
//                    booking.getTimeSlot()
//            );
//            booking.setStatus("ASSIGNED");
//        } else {
//            booking.setStatus("PENDING");
//        }

        return bookingRepo.save(booking);
    }

    public List<Booking> getPendingBookings() {
        return bookingRepo.findByAssigned(false);
    }


    public Booking assignTechnician(int bookingId, int technicianId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.isAssigned()) {
            throw new BadRequestException("Only UNASSIGNED bookings can be assigned");
        }

        Technician technician = technicianRepo.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found"));

        LaserService ls = serviceRepo.findById(booking.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        if (technician.getAvailablePinCodes() == null
                || !technician.getAvailablePinCodes().contains(booking.getPinCode())) {
            throw new BadRequestException(
                    "Technician does not serve pin code " + booking.getPinCode()
            );
        }

        boolean validForService = false;
        for (String pin : ls.getValidPinCodes()) {
            if (pin.equals(booking.getPinCode())) {
                validForService = true;
                break;
            }
        }
        if (!validForService) {
            throw new BadRequestException(
                    "Service is not available in pin code " + booking.getPinCode()
            );
        }

        boolean isAvailable = technicianService.isTechnicianAvailable(
                technicianId,
                booking.getDate(),
                booking.getTimeSlot()
        );
        if (!isAvailable) {
            throw new BadRequestException("Technician is not available for that date/time");
        }

        technicianService.bookSlot(
                technicianId,
                booking.getDate(),
                booking.getTimeSlot()
        );

        booking.setTechnicianId(technicianId);
        booking.setCompleted(true);
        return bookingRepo.save(booking);
    }

    public List<Booking> getBookingsForDate(LocalDate date) {
        return bookingRepo.findByDate(date);
    }
}

