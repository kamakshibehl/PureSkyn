package com.self.PureSkyn.Model.response;

import com.self.PureSkyn.Model.BookingStatus;
import com.self.PureSkyn.Model.Payment;
import com.self.PureSkyn.Model.request.BookingServiceInfoDTO;
import com.self.PureSkyn.Model.Beneficiary;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDTO {
    private String bookingId;
    private String userId;
    private String technicianId;
    private String technicianName;
    private Beneficiary userInfo;
    private List<BookingServiceInfoDTO> servicesBooked;

    private Payment payment;
    private BookingStatus status;
    private LocalDateTime createdAt;
}
