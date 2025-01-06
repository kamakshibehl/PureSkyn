package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.Facility;
import com.self.PureSkyn.repository.ServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    @Autowired
    ServiceRepo serviceRepo;

    @GetMapping("/all")
    public List<Facility> getAllServices() {
        return serviceRepo.findAll();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Facility createService(@RequestBody Facility facility) {
        return serviceRepo.save(facility);
    }
}

