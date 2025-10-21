package com.sportspro.controller;

import com.sportspro.dto.OpportunityDTO;
import com.sportspro.service.OpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    @Autowired
    private OpportunityService opportunityService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public OpportunityDTO postOpportunity(@RequestBody OpportunityDTO dto) {
        return opportunityService.createOpportunity(dto);
    }

    @GetMapping
    public List<OpportunityDTO> getAllOpportunities() {
        return opportunityService.getAllOpportunities();
    }
}
