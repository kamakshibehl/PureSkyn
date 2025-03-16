package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Getter
@Setter
public class BookingDTO {
    private String bookingId;
    private String userId;
    private String technicianId;
    private String technicianName;
    private BookingUserInfoDTO userInfo;
    private List<BookingServiceInfoDTO> servicesBooked;

    private Payment payment;
    private BookingStatus status;
    private LocalDateTime createdAt;
}
