package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/query")
public class QueryController {

    @Autowired
    QueryService queryService;

    @PostMapping("/new")
    public ResponseEntity<ApiResponse<String>> createQuery(@RequestBody QueryDTO query) {
        try {
            String createdQuery = queryService.createQuery(query);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Query created successfully", createdQuery));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while creating the query"));
        }
    }

    @PatchMapping("/{queryId}/status")
    public ResponseEntity<ApiResponse<Query>> updateQueryStatus(
            @PathVariable String queryId,
            @RequestParam QueryStatus status) {
        try {
            Query updatedQuery = queryService.updateQueryStatus(queryId, status);
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Query status updated", updatedQuery));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    ApiResponseStatus.FAIL,
                    "Query not found.",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while updating query status"));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Query>>> getAllQueries() {
        try {
            List<Query> queries = queryService.getAllQueries();
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "All queries fetched", queries));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while fetching queries"));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Query>>> getQueriesByStatus(@PathVariable QueryStatus status) {
        try {
            List<Query> queries = queryService.getQueriesByStatus(status);
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Queries fetched by status", queries));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while fetching queries"));
        }
    }

    @GetMapping("/statuses")
    public ResponseEntity<ApiResponse<List<QueryStatus>>> getAllQueryStatuses() {
        try {
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Query statuses fetched", Arrays.asList(QueryStatus.values())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ApiResponseStatus.ERROR, "An error occurred while fetching queries"));
        }
    }
}
