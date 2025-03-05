package com.self.PureSkyn.service;

import com.self.PureSkyn.Model.Query;
import com.self.PureSkyn.Model.QueryDTO;
import com.self.PureSkyn.Model.QueryStatus;
import com.self.PureSkyn.repository.QueryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QueryService {

    @Autowired
    private QueryRepo queryRepo;

    public String createQuery(QueryDTO queryDTO) {
        Query query = new Query();
        query.setName(queryDTO.getName());
        query.setPhone(queryDTO.getPhone());
        query.setServiceId(queryDTO.getServiceId());
        query.setQueryStatus(QueryStatus.NEW);
        query.setSubmittedAt(LocalDateTime.now());

        queryRepo.save(query);

        return "Query submitted successfully.";
    }

    public Query updateQueryStatus(String queryId, QueryStatus newStatus) {
        Query query = queryRepo.findById(queryId)
                .orElseThrow(() -> new IllegalArgumentException("Query not found"));

        query.setQueryStatus(newStatus);
        return queryRepo.save(query);
    }

    public List<Query> getAllQueries() {
        return queryRepo.findAll();
    }

    public List<Query> getQueriesByStatus(QueryStatus status) {
        return queryRepo.findByQueryStatus(status);
    }
}
