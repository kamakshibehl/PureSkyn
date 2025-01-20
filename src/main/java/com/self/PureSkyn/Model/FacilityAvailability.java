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
}
