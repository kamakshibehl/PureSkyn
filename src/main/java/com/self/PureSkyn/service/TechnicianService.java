package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Technician;
import com.self.PureSkyn.Model.TechnicianAvailability;
import com.self.PureSkyn.exception.ConflictException;
import com.self.PureSkyn.repository.TechnicianAvailablityRepo;
import com.self.PureSkyn.repository.TechnicianRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TechnicianService {

    @Autowired
    private TechnicianRepo technicianRepo;
    @Autowired
    private TechnicianAvailablityRepo availablityRepo;

    public List<Technician> getAllTechnicians() {
        return technicianRepo.findAll();
    }

    public Technician getTechnician(int id) {
        return technicianRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
    }

    public boolean isTechnicianAvailable(int technicianId, LocalDate date, LocalTime timeSlot) {
        var existing = availablityRepo.findByTechnicianIdAndDateAndTimeSlot(
                technicianId, date, timeSlot
        );
        return existing.isEmpty();
    }

    public void bookSlot(int technicianId, LocalDate date, LocalTime timeSlot) {
        if (!isTechnicianAvailable(technicianId, date, timeSlot)) {
            throw new ConflictException("Technician is already booked for that date/time");
        }
        TechnicianAvailability ta = new TechnicianAvailability();
        ta.setId(technicianId);
        ta.setDate(date);
        ta.setTimeSlot(timeSlot);
        availablityRepo.save(ta);
    }

    public List<Technician> getAvailableTechnicians(String serviceId, LocalDate date, LocalTime timeSlot) {
        List<Technician> technicians = technicianRepo.findByServiceTypesContaining(serviceId);

        return technicians.stream()
                .filter(technician -> availablityRepo.findByTechnicianIdAndDateAndTimeSlot(
                        technician.getId(), date, timeSlot).isEmpty())
                .toList();
    }
}
