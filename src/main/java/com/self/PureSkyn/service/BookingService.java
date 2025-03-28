package com.self.PureSkyn.service;

import com.razorpay.Order;
import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.Model.request.BookingRequest;
import com.self.PureSkyn.Model.request.BookingServiceInfoDTO;
import com.self.PureSkyn.Model.response.BookingDTO;
import com.self.PureSkyn.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private PaymentService paymentService;

//    private final List<Facility>
    private final Map<String, Integer> subserviceToSessions = new HashMap<>();

    BookingService(FacilityRepo facilityRepo) {
//        subserviceToSessions = new HashMap<>();

        List<Facility> facilities = facilityRepo.findAll();
        for(Facility facility: facilities) {
            for (FacilityTypes facilityTypes: facility.getTypes()) {
                subserviceToSessions.put(facilityTypes.getSubServiceId(), facilityTypes.getNoOfSessions());
            }
        }

    }

//    @PostConstruct
//    public void initSubserviceSessions() {
//        List<Facility> facilities = facilityRepo.findAll();
//        for (Facility facility : facilities) {
//            for (FacilityTypes facilityTypes : facility.getTypes()) {
//                subserviceToSessions.put(facilityTypes.getSubServiceId(), facilityTypes.getNoOfSessions());
//            }
//        }
//    }


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

    public BookingDTO createBooking(BookingRequest bookingRequest) {

        Booking booking = new Booking();
        booking.setUserId(bookingRequest.getUserId());
        booking.setBeneficiary(bookingRequest.getBeneficiary());
        booking.setServicesBooked(bookingRequest.getServicesBooked());
        booking.setPaymentId(booking.getPaymentId());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);

        List<BookingServiceInfoDTO> services =  booking.getServicesBooked();
        for(BookingServiceInfoDTO service: services) {

            int noOfSessions = subserviceToSessions.getOrDefault(service.getSubServiceId(), 1);

            List<Session> sessions = new ArrayList<>();
            for (int i=0; i<noOfSessions; i++) {
                Session session = new Session();
                if(i == 0) {
                    session.setDate(service.getDate());
                    session.setTime(service.getTime());
                }
                sessions.add(sessionRepo.save(session));
            }

            service.setSessions(sessions);
        }

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
//        List<Booking> bookings = bookingRepo.findByBeneficiary_TreatmentDate(date);
        List<Booking> bookings = new ArrayList<>();
        return bookings.stream()
                .map(this::convertToBookingDTO)
                .toList();
    }

    public List<BookingDTO> getAllBookingsInDescOrder() {
//        List<Booking> bookings = bookingRepo.findAllByOrderByUserInfo_TreatmentDateDesc();
        List<Booking> bookings = new ArrayList<>();
        return bookings.stream().map(this::convertToBookingDTO).toList();
    }

    private BookingDTO convertToBookingDTO(Booking booking) {
        Payment payment = paymentService.getPaymentDetail(booking.getPaymentId()).orElseThrow();

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
        dto.setUserInfo(booking.getBeneficiary());
        dto.setServicesBooked(booking.getServicesBooked());

        dto.setPayment(payment);
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

