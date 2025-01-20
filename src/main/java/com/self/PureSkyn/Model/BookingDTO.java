package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
public class BookingDTO {
    @Id
    private String id;
    private String userId;
    private String userName;
    private String technicianId;
    private String technicianName;
    private String serviceId;
    private String serviceName;
    private String address;
    private String pinCode;
    private Payment payment;

    private LocalDate date;
    private LocalTime timeSlot;
    private BookingStatus status;
}
