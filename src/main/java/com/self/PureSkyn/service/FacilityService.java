package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.ApiResponse;
import com.self.PureSkyn.Model.ApiResponseStatus;
import com.self.PureSkyn.Model.Facility;
import com.self.PureSkyn.Model.FacilityTypes;
import com.self.PureSkyn.exception.ResourceNotFoundException;
import com.self.PureSkyn.repository.FacilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FacilityService {

    @Autowired
    FacilityRepo facilityRepo;


//    public Facility createFacility(Facility facility) {
//        if (facilityRepo.existsByName(facility.getName())) {
//            throw new IllegalArgumentException("Service with the same name already exists.");
//        }
//        return facilityRepo.save(facility);
//    }

    public List<Facility> getAllFacilities() {
        return facilityRepo.findAll();
    }

    public List<LocalTime> getAvailableTimeSlots(String serviceId, LocalDate date) {
        return List.of(
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                LocalTime.of(18, 0)
        );
    }

    private String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }

//
//    public void saveFacilities(List<Facility> facilities) {
//        for (Facility facility : facilities) {
//            if (facility.getTypes() != null) {
//                for (FacilityTypes type : facility.getTypes()) {
//                    if (type.getServiceId() == null) {
//                        type.setServiceId(UUID.randomUUID().toString());
//                    }
//                }
//            }
//        }
//        facilityRepo.saveAll(facilities);
//    }


}
