package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends MongoRepository<Cart, String> {
    List<Cart> findByUserId(String userId);
    Optional<Cart> findByUserIdAndServiceIdAndSubServiceId(String userId, String serviceId, String subServiceId);
    void deleteByUserId(String userId);
}
