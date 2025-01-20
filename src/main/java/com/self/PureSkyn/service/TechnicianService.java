package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Technician;
import com.self.PureSkyn.Model.TechnicianAvailability;
import com.self.PureSkyn.Model.TechnicianDTO;
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

    public Technician getTechnician(String id) {
        return technicianRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
    }

    public boolean isTechnicianAvailable(String technicianId, LocalDate date, LocalTime timeSlot) {
        var existing = availablityRepo.findByTechnicianIdAndDateAndTimeSlot(
                technicianId, date, timeSlot
        );
        return existing.isEmpty();
    }

    public void bookSlot(String technicianId, LocalDate date, LocalTime timeSlot) {
        if (!isTechnicianAvailable(technicianId, date, timeSlot)) {
            throw new ConflictException("Technician is already booked for that date/time");
        }
        TechnicianAvailability ta = new TechnicianAvailability();
        ta.setId(technicianId);
        ta.setDate(date);
        ta.setTimeSlot(timeSlot);
        availablityRepo.save(ta);
    }

    public List<TechnicianDTO> getAvailableTechnicians(String serviceId, LocalDate date, LocalTime timeSlot) {
        List<Technician> technicians = technicianRepo.findByServiceTypesContaining(serviceId);

        return technicians.stream()
                .filter(technician -> availablityRepo.findByTechnicianIdAndDateAndTimeSlot(
                        technician.getId(), date, timeSlot).isEmpty())
                .map(this::convertToTechnicianDTO)
                .toList();
    }

    private TechnicianDTO convertToTechnicianDTO(Technician technician) {
        TechnicianDTO dto = new TechnicianDTO();
        dto.setId(technician.getId());
        dto.setFirstName(technician.getFirstName());
        dto.setLastName(technician.getLastName());
        dto.setEmail(technician.getEmail());
        dto.setPhone(technician.getPhone());
        return dto;
    }


}
