package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String userId;
    private String technicianId;
    private String serviceId;

    private String addressId;
    private String pinCode;
    private Payment payment;

    private LocalDate date;
    private LocalTime timeSlot;
    private BookingStatus status;
}
