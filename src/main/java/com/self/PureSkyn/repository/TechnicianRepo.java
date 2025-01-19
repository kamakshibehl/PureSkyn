package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Technician;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TechnicianRepo extends MongoRepository<Technician, Integer> {
    List<Technician> findByServiceTypesContaining(String serviceId);
    // List<Technician> findByAvailablePinCodesContains(String pinCode);
}