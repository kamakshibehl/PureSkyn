package com.self.PureSkyn.Model;

import com.self.PureSkyn.Model.request.BookingServiceInfoDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String userId;
    private String technicianId;

    private Beneficiary beneficiary;
    private List<BookingServiceInfoDTO> servicesBooked;

    private String paymentId;
    private BookingStatus status;

    @CreatedDate
    private LocalDateTime createdAt;
}
