package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Data
public class BookingRequestResponseDTO {
    private PriceDetailsDTO priceDetails;
    private List<LocalTime> availableTimeSlots;

    public BookingRequestResponseDTO(PriceDetailsDTO priceDetails, List<LocalTime> availableTimeSlots) {
        this.priceDetails = priceDetails;
        this.availableTimeSlots = availableTimeSlots;
    }

    public PriceDetailsDTO getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(PriceDetailsDTO priceDetails) {
        this.priceDetails = priceDetails;
    }

    public List<LocalTime> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<LocalTime> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }
}
