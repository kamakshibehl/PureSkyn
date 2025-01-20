package com.self.PureSkyn.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "technician_availability")
public class TechnicianAvailability {
    @Id
    private String id;
    private String technicianId;
    private LocalDate date;
    private LocalTime timeSlot;
}
