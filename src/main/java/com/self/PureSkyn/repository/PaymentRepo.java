package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepo extends MongoRepository<Payment, String> {
}
