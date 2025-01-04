package com.self.PureSkyn.Model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Booking {
    private int id;
    private int userId;
    private int technicianId;
    private int serviceId;
    private String pinCode;

    private LocalDate date;
    private LocalTime timeSlot;
    private boolean isAssigned = false;
    private boolean isCompleted = false;

//    private Session session;
//    private String status;
//    private String createdDate;
//    private String lastModifiedDate;
//    private String expiryDate;
}
