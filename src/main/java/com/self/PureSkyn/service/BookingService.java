package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.exception.BadRequestException;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private FacilityRepo serviceRepo;

    @Autowired
    private TechnicianRepo technicianRepo;

    @Autowired TechnicianAvailablityRepo technicianAvailablityRepo;

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    public PriceDetailsDTO requestBooking(String serviceId, LocalDate date, LocalTime timeSlot) {
        LocalDate today = LocalDate.now();
        if (date == null || timeSlot == null) {
            throw new BadRequestException("Date and timeSlot must be specified");
        }
        if (date.isBefore(today)) {
            throw new BadRequestException("Cannot book a date in the past");
        }

        Facility facility = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        boolean technicianAvailable = technicianService.isTechnicianAvailable(serviceId, date, timeSlot);
        if (!technicianAvailable) {
            throw new BadRequestException("No technicians are available for the selected time slot");
        }

        double halfPrice = facility.getPrice() * 0.5;
        double fullPrice = facility.getPrice();

        return new PriceDetailsDTO(halfPrice, fullPrice);
    }

    public Booking createBooking(Booking booking) {
        LocalDate today = LocalDate.now();
        if (booking.getDate() == null || booking.getTimeSlot() == null) {
            throw new BadRequestException("Date and timeSlot must be specified");
        }
        if (booking.getDate().isBefore(today)) {
            throw new BadRequestException("Cannot book a date in the past");
        }

        Facility facility = serviceRepo.findById(booking.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        double requiredPayment = facility.getPrice() * 0.5;
        Payment paymentDetails = booking.getPayment();

        if (paymentDetails == null || paymentDetails.getAmountPaid() < requiredPayment) {
            throw new BadRequestException("At least 50% payment is required to book the service");
        }

        paymentDetails.setBookingId(booking.getId());
        paymentDetails.setFullPayment(paymentDetails.getAmountPaid() >= facility.getPrice());
        paymentDetails.setPaymentDate(LocalDateTime.now());

        booking.setStatus(BookingStatus.PENDING);
        booking.setPayment(paymentDetails);

        return bookingRepo.save(booking);
    }

    public BookingDTO assignTechnician(String bookingId, String technicianId) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!BookingStatus.PENDING.equals(booking.getStatus())) {
            throw new BadRequestException("Booking is not in pending status");
        }

        Technician technician = technicianRepo.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found"));

        boolean isAvailable = technicianAvailablityRepo.findByTechnicianIdAndDateAndTimeSlot(
                technicianId, booking.getDate(), booking.getTimeSlot()).isEmpty();

        if (!isAvailable) {
            throw new BadRequestException("Technician is not available for the selected date and time slot");
        }

        booking.setTechnicianId(technicianId);
        booking.setStatus(BookingStatus.ASSIGNED);
        Booking updatedBooking = bookingRepo.save(booking);

        TechnicianAvailability availability = new TechnicianAvailability();
        availability.setId(technicianId);
        availability.setDate(booking.getDate());
        availability.setTimeSlot(booking.getTimeSlot());
        technicianAvailablityRepo.save(availability);

        return updatedBooking.stream().map(this::convertToBookingDTO).toList();
    }

    public List<BookingDTO> getAllPendingBookings() {
        return bookingRepo.findByStatus(BookingStatus.PENDING)
                .stream()
                .map(this::convertToBookingDTO)
                .toList();
    }

    public List<BookingDTO> getBookingsByDate(LocalDate date) {
        List<Booking> bookings = bookingRepo.findByDate(date);
        return bookings.stream()
                .map(this::convertToBookingDTO)
                .toList();
    }

    public List<BookingDTO> getAllBookingsInDescOrder() {
        List<Booking> bookings = bookingRepo.findAll();
        return bookings.stream().map(this::convertToBookingDTO).toList();
    }

    private BookingDTO convertToBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());

        dto.setServiceName(
                serviceRepo.findById(booking.getServiceId())
                        .map(Facility::getName)
                        .orElse("Unknown Service")
        );

        dto.setUserName(
                userRepo.findById(booking.getUserId())
                        .map(User::getName)
                        .orElse("Unknown User")
        );

        dto.setTechnicianName(
                booking.getTechnicianId() != null
                        ? technicianRepo.findById(booking.getTechnicianId())
                        .map(Technician::getName)
                        .orElse("Unknown Technician")
                        : "Not Assigned"
        );

        dto.setAddress(
                addressRepo.findById(String.valueOf(booking.getAddressId()))
                        .map(this::formatAddress)
                        .orElse("Unknown Address")
        );

        dto.setDate(booking.getDate());
        dto.setTimeSlot(booking.getTimeSlot());
        dto.setStatus(BookingStatus.valueOf(booking.getStatus().name())); // Assuming `getStatus()` is an enum

        return dto;
    }

    private String formatAddress(Address address) {
        return String.format("%s, %s, %s, %s, %s, %s",
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPinCode());
    }

    public List<BookingDTO> getBookingsByUserId(String userId) {
        List<Booking> bookings = bookingRepo.findByUserId(userId);

        return bookings.stream().map(this::convertToBookingDTO).toList();
    }

    public Booking markBookingAsCompleted(String bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (BookingStatus.COMPLETED.equals(booking.getStatus())) {
            throw new IllegalStateException("Booking is already completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        return bookingRepo.save(booking);
    }

    public Booking markBookingAsCancelled(String bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (BookingStatus.CANCELLED.equals(booking.getStatus())) {
            throw new IllegalStateException("Booking is already canceled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepo.save(booking);
    }
}

