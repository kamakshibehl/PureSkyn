package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserUpdateRepo extends MongoRepository<User, Integer> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
