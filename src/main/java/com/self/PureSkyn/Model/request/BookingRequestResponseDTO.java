package com.self.PureSkyn.Model.request;

import com.self.PureSkyn.Model.PriceDetailsDTO;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class BookingRequestResponseDTO {
    private PriceDetailsDTO priceDetails;
    private List<LocalTime> availableTimeSlots;

}
