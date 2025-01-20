package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepo extends MongoRepository<Admin, String> {
    Admin findByUsername(String username);

    Admin findByEmail(String email);

    boolean existsByEmail(String email);
}
