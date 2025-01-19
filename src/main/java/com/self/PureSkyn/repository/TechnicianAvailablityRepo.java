package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.TechnicianAvailability;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface TechnicianAvailablityRepo extends MongoRepository<TechnicianAvailability, Integer> {

    Optional<TechnicianAvailability> findByTechnicianIdAndDateAndTimeSlot(int technicianId, LocalDate date, LocalTime timeSlot);
}
