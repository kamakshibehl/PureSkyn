package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepo extends MongoRepository<Session, String> {
}
