package com.sportspro.mapper;

import com.sportspro.dto.OpportunityDTO;
import com.sportspro.model.Opportunity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OpportunityMapper {

    // Map Opportunity entity to DTO
    @Mapping(target = "postedByName", source = "user.name")
    @Mapping(target = "postedByAvatar", source = "user.avatar")
    @Mapping(target = "postedAt", source = "postedAt")  // ✅ Include this to send time to frontend
    OpportunityDTO opportunityToOpportunityDTO(Opportunity opportunity);

    // Map DTO to Opportunity entity
    @Mapping(target = "opportunityId", ignore = true) // Let JPA generate it
    @Mapping(target = "user", ignore = true)          // Set in service based on token
    @Mapping(target = "postedAt", ignore = true)      // Set in service
    Opportunity opportunityDTOToOpportunity(OpportunityDTO dto);
}
