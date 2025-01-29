package com.self.PureSkyn.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Getter
@Setter
@Document(collection = "facility_availability")
public class FacilityAvailability {
    @Id
    private String id;
    private String serviceId;
    private LocalDate date;
    private List<LocalTime> unavailableSlots;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<LocalTime> getUnavailableSlots() {
        return unavailableSlots;
    }

    public void setUnavailableSlots(List<LocalTime> unavailableSlots) {
        this.unavailableSlots = unavailableSlots;
    }
}
