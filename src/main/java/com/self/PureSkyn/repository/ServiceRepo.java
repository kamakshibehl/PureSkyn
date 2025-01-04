package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.LaserService;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepo extends MongoRepository<LaserService, Integer> {
}
