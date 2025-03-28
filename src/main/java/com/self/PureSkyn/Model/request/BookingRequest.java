package com.self.PureSkyn.Model.request;

import com.self.PureSkyn.Model.Beneficiary;
import com.self.PureSkyn.Model.Payment;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
    private String userId;

    private Beneficiary beneficiary;
    private List<BookingServiceInfoDTO> servicesBooked;

    private String paymentId;
}
