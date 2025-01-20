package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Address;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface AddressRepo extends MongoRepository<Address, String> {
    List<Address> findByUserId(String userId);
}