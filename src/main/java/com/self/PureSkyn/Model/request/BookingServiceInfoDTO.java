package com.self.PureSkyn.Model.request;

import com.self.PureSkyn.Model.Session;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BookingServiceInfoDTO {
    private String subServiceId;
    private LocalDate date;
    private LocalDateTime time;
    private List<Session> sessions;

    BookingServiceInfoDTO(String subServiceId, LocalDate date, LocalDateTime time) {
        this.subServiceId = subServiceId;
        this.date = date;
        this.time = time;
    }
}

// booking -> bookingrequest => user detail + multiple services + time-slots of first session of each service
// service -> multiple sessions + timeslot book

// userinfo + List<Facility>

// 1 Facility => Service + List<Multiple Subservices>

// 1 Subservice => Multiple Session



// userinfo + List<Facility>

// 1 Facility => Service + List<Multiple Subservices>

// 1 Subservice => Multiple Session


// Subservice -> Service, sessions

// service 1...*   subservice
// Service A    sub-s, sub-t
// Service B    sub-p, sub-q





