package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Facility;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FacilityRepo extends MongoRepository<Facility, Integer> {
        boolean existsByName(String name);
}
