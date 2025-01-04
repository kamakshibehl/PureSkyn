package com.self.PureSkyn.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "technician_availability")
public class TechnicianAvailability {
    @Id
    private int id;
    private LocalDate date;
    private LocalTime timeSlot;
}
