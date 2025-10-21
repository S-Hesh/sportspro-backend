package com.sportspro.service;

import com.sportspro.dto.OpportunityDTO;
import com.sportspro.mapper.OpportunityMapper;
import com.sportspro.model.Opportunity;
import com.sportspro.model.User;
import com.sportspro.repository.OpportunityRepository;
import com.sportspro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpportunityService {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private OpportunityMapper opportunityMapper;

    @Autowired
    private UserRepository userRepository;

    // Post a new opportunity
    public OpportunityDTO createOpportunity(OpportunityDTO dto) {
        Opportunity opportunity = opportunityMapper.opportunityDTOToOpportunity(dto);

        // Get current authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        opportunity.setUser(user);
        opportunity.setPostedAt(LocalDateTime.now());

        Opportunity saved = opportunityRepository.save(opportunity);
        return opportunityMapper.opportunityToOpportunityDTO(saved);
    }
    //getall oppo
    public List<OpportunityDTO> getAllOpportunities() {
        return opportunityRepository.findAll().stream()
                .map(opportunityMapper::opportunityToOpportunityDTO)
                .collect(Collectors.toList());
    }

    // Get opportunities by sport
    public List<OpportunityDTO> getOpportunitiesBySport(String sport) {
        List<Opportunity> opportunities = opportunityRepository.findBySport(sport);
        return opportunities.stream()
                .map(opportunityMapper::opportunityToOpportunityDTO)  // Convert each opportunity to DTO
                .collect(Collectors.toList());
    }
}
