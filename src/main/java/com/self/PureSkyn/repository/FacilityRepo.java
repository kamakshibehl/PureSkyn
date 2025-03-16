package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Facility;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FacilityRepo extends MongoRepository<Facility, String> {

}
