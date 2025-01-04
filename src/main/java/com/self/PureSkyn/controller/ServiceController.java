package com.self.PureSkyn.controller;

import com.self.PureSkyn.Model.LaserService;
import com.self.PureSkyn.repository.ServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    ServiceRepo serviceRepo;

    @GetMapping
    public List<LaserService> getAllServices() {
        return serviceRepo.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LaserService createService(@RequestBody LaserService laserService) {
        return serviceRepo.save(laserService);
    }
}

