package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Technician;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TechnicianRepo extends MongoRepository<Technician, Integer> {
    // List<Technician> findByAvailablePinCodesContains(String pinCode);
}