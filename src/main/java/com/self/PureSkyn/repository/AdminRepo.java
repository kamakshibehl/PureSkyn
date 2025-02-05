package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Admin;
import com.self.PureSkyn.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepo extends MongoRepository<Admin, String> {
    //Optional<User> findByUsername(String username);
    //boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByPhone(String phone);
}
