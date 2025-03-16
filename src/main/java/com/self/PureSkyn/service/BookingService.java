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
import java.util.Optional;

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
    private FacilityService facilityService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private FacilityRepo facilityRepo;


//    public BookingRequestResponseDTO requestBooking(String id, String serviceId, LocalDate date) {
//        LocalDate today = LocalDate.now();
//
//        if (date.isBefore(today)) {
//            throw new BadRequestException("Cannot book a date in the past");
//        }
//
//        Facility facility = facilityRepo.findByServiceId(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Service with serviceId: " + id + " not found"));
//
////        boolean serviceExists = facility.getTypes().stream()
////                .anyMatch(type -> type.getSubServiceId().equals(serviceId));
//
//        Optional<FacilityTypes> subService = facility.getTypes().stream()
//                .filter(type -> type.getSubServiceId().equals(serviceId))
//                .findFirst();
//
//
//        if (subService.isEmpty()) {
//            throw new ResourceNotFoundException("Sub Service with id: " + serviceId + " not found in Service with id: " + id);
//        }
//
//        List<LocalTime> availableTimeSlots = facilityService.getAvailableTimeSlots(serviceId, date);
//
//        double halfPrice = subService.get().getPrice() * 0.5;
//        double fullPrice = subService.get().getPrice();
//
//        PriceDetailsDTO priceDetails = new PriceDetailsDTO(halfPrice, fullPrice);
//
//        return new BookingRequestResponseDTO(priceDetails, availableTimeSlots);
//    }

    public BookingDTO createBooking(Booking bookingRequest) {
        if (bookingRequest.getUserInfo().getTreatmentDate() == null || bookingRequest.getUserInfo().getTimeSlot() == null) {
            throw new BadRequestException("Date and timeSlot must be specified");
        }
        Booking booking = new Booking();
        booking.setTechnicianId(bookingRequest.getTechnicianId());
        booking.setUserInfo(bookingRequest.getUserInfo());
        booking.setServicesBooked(bookingRequest.getServicesBooked());
        booking.setPayment(bookingRequest.getPayment());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);

        return convertToBookingDTO(bookingRepo.save(booking));
    }


//    public BookingDTO assignTechnician(String bookingId, String technicianId) {
//
//        Booking booking = bookingRepo.findById(bookingId)
//                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
//
//        if (!BookingStatus.PENDING.equals(booking.getStatus())) {
//            throw new BadRequestException("Booking is not in pending status");
//        }
//
//        Technician technician = technicianRepo.findById(technicianId)
//                .orElseThrow(() -> new ResourceNotFoundException("Technician not found"));
//
//        boolean isAvailable = technicianAvailablityRepo.findByTechnicianIdAndDateAndTimeSlot(
//                technicianId, booking.getDate(), booking.getTimeSlot()).isEmpty();
//
//        if (!isAvailable) {
//            throw new BadRequestException("Technician is not available for the selected date and time slot");
//        }
//
//        booking.setTechnicianId(technicianId);
//        booking.setStatus(BookingStatus.ASSIGNED);
//        Booking updatedBooking = bookingRepo.save(booking);
//
//        TechnicianAvailability availability = new TechnicianAvailability();
//        availability.setId(technicianId);
//        availability.setDate(booking.getDate());
//        availability.setTimeSlot(booking.getTimeSlot());
//        technicianAvailablityRepo.save(availability);
//
//        return convertToBookingDTO(updatedBooking);
//    }

    public List<BookingDTO> getBookingsByStatus(BookingStatus status) {
        return bookingRepo.findByStatus(status)
                .stream()
                .map(this::convertToBookingDTO)
                .toList();
    }

    public List<BookingDTO> getBookingsByDate(LocalDate date) {
        List<Booking> bookings = bookingRepo.findByUserInfo_TreatmentDate(date);
        return bookings.stream()
                .map(this::convertToBookingDTO)
                .toList();
    }

    public List<BookingDTO> getAllBookingsInDescOrder() {
        List<Booking> bookings = bookingRepo.findAllByOrderByUserInfo_TreatmentDateDesc();
        return bookings.stream().map(this::convertToBookingDTO).toList();
    }

    private BookingDTO convertToBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getId());
        dto.setUserId(booking.getUserId());
        dto.setTechnicianId(booking.getTechnicianId());
        dto.setTechnicianName(
                booking.getTechnicianId() != null
                        ? technicianRepo.findById(booking.getTechnicianId())
                        .map(Technician::getName)
                        .orElse("Unknown Technician")
                        : "Not Assigned"
        );
        dto.setUserInfo(booking.getUserInfo());
        dto.setServicesBooked(booking.getServicesBooked());

        dto.setPayment(booking.getPayment());
        dto.setStatus(booking.getStatus());
        dto.setCreatedAt(booking.getCreatedAt());

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

    public Booking updateBookingStatus(String bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus().equals(newStatus)) {
            throw new IllegalStateException("Booking is already " + newStatus);
        }

        booking.setStatus(newStatus);
        return bookingRepo.save(booking);
    }
}

