package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.*;
import com.self.PureSkyn.repository.FacilityRepo;
import com.self.PureSkyn.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class FacilityController {

    @Autowired
    FacilityRepo serviceRepo;

    @Autowired
    FacilityService facilityService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Facility>>> getAllFacilities() {
        try {
            List<Facility> services = facilityService.getAllFacilities();
            if (services.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>(
                        ApiResponseStatus.FAIL,
                        "No services found.",
                        null
                ));
            }
            return ResponseEntity.ok(new ApiResponse<>(ApiResponseStatus.SUCCESS, "Services retrieved successfully", services));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(ApiResponseStatus.ERROR, "An unexpected error occurred")
            );
        }
    }

//    @PostMapping("/create")
//    @PreAuthorize("@adminService.isCurrentUserAdmin()")
//    public ResponseEntity<Facility> createService(@RequestBody Facility facility) {
//        try {
//            Facility createdFacility = facilityService.createFacility(facility);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdFacility);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

}

