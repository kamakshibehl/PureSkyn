package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Facility;
import com.self.PureSkyn.repository.FacilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {

    @Autowired
    FacilityRepo facilityRepo;

    public Facility createFacility(Facility facility) {
        if (facilityRepo.existsByName(facility.getName())) {
            throw new IllegalArgumentException("Service with the same name already exists.");
        }
        return facilityRepo.save(facility);
    }

}
