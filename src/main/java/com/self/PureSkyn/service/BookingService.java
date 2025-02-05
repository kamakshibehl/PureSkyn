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
    private FacilityService facilityService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private FacilityRepo facilityRepo;


    public BookingRequestResponseDTO requestBooking(String id, String serviceId, LocalDate date) {
        LocalDate today = LocalDate.now();

        if (date.isBefore(today)) {
            throw new BadRequestException("Cannot book a date in the past");
        }

        Facility facility = facilityRepo.findByServiceId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service with serviceId: " + id + " not found"));

        boolean serviceExists = facility.getTypes().stream()
                .anyMatch(type -> type.getSubServiceId().equals(serviceId));

        if (!serviceExists) {
            throw new ResourceNotFoundException("Sub Service with id: " + serviceId + " not found in Service with id: " + id);
        }

//        boolean technicianAvailable = technicianService.isTechnicianAvailable(serviceId, date, timeSlot);
//        if (!technicianAvailable) {
//            throw new BadRequestException("No technicians are available for the selected time slot");
//        }

        List<LocalTime> availableTimeSlots = facilityService.getAvailableTimeSlots(serviceId, date);

        double halfPrice = facility.getPrice() * 0.5;
        double fullPrice = facility.getPrice();

        PriceDetailsDTO priceDetails = new PriceDetailsDTO(halfPrice, fullPrice);

        return new BookingRequestResponseDTO(priceDetails, availableTimeSlots);
    }

    public BookingDTO createBooking(Booking bookingRequest) {
        LocalDate today = LocalDate.now();

        if (bookingRequest.getDate() == null || bookingRequest.getTimeSlot() == null) {
            throw new BadRequestException("Date and timeSlot must be specified");
        }
        if (bookingRequest.getDate().isBefore(today)) {
            throw new BadRequestException("Cannot book a date in the past");
        }

        Facility facility = serviceRepo.findByServiceId(bookingRequest.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        FacilityTypes subFacility = facility.getTypes().stream()
                .filter(type -> type.getSubServiceId().equals(bookingRequest.getSubServiceId()))  // Filter by subServiceId
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Sub service not found"));  // Throw exception if not found


        double requiredPayment = facility.getPrice() * 0.5;
        Payment paymentDetails = bookingRequest.getPayment();

        if (paymentDetails == null || paymentDetails.getAmountPaid() < requiredPayment) {
            throw new BadRequestException("At least 50% payment is required to book the service");
        }

        Booking booking = new Booking();
        booking.setUserId(bookingRequest.getUserId());
        booking.setServiceId(bookingRequest.getServiceId());
        booking.setSubServiceId(bookingRequest.getSubServiceId());
        booking.setDate(bookingRequest.getDate());
        booking.setTimeSlot(bookingRequest.getTimeSlot());
        booking.setTechnicianId(bookingRequest.getTechnicianId());
        booking.setAddressId(bookingRequest.getAddressId());
        booking.setStatus(BookingStatus.PENDING);

        Payment payment = new Payment();
        payment.setAmountPaid(paymentDetails.getAmountPaid());
        payment.setFullPayment(paymentDetails.getAmountPaid() >= facility.getPrice());
        payment.setPaymentDate(LocalDateTime.now());

        booking.setPayment(payment);

        Booking savedBooking = bookingRepo.save(booking);

        return convertToBookingDTO(savedBooking);
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

        return convertToBookingDTO(updatedBooking);
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
                serviceRepo.findByServiceId(booking.getServiceId())
                        .map(Facility::getName)
                        .orElse("Unknown Service")
        );

        dto.setSubServiceName(
                serviceRepo.findByServiceId(booking.getServiceId())
                        .flatMap(facility -> facility.getTypes().stream()
                                .filter(type -> type.getSubServiceId().equals(booking.getSubServiceId()))
                                .findFirst())
                        .map(FacilityTypes::getName)
                        .orElse("Unknown SubService")
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
                addressRepo.findById(booking.getAddressId())
                        .map(this::formatAddress)
                        .orElse("Unknown Address")
        );

        dto.setPinCode(booking.getPinCode());

        dto.setPayment(booking.getPayment());

        dto.setDate(booking.getDate());
        dto.setTimeSlot(booking.getTimeSlot());

        dto.setStatus(booking.getStatus());

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

