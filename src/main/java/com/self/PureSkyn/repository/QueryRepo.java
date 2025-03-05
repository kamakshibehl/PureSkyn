package com.self.PureSkyn.repository;

import com.self.PureSkyn.Model.Query;
import com.self.PureSkyn.Model.QueryStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QueryRepo extends MongoRepository<Query, String> {
    List<Query> findByQueryStatus(QueryStatus queryStatus);
}
