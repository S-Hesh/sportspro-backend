package com.sportspro.service;

import com.sportspro.dto.SkillDTO;
import com.sportspro.mapper.SkillMapper;
import com.sportspro.model.Skill;
import com.sportspro.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    // Add a new skill
    public SkillDTO addSkill(SkillDTO skillDTO) {
        Skill skill = skillMapper.skillDTOToSkill(skillDTO); // Convert DTO to entity
        skill = skillRepository.save(skill);  // Save the skill
        return skillMapper.skillToSkillDTO(skill); // Return the saved skill as DTO
    }

    // Get skills by userId
    public List<SkillDTO> getSkillsByUserId(Long userId) {
        List<Skill> skills = skillRepository.findByUserUserId(userId);
        return skills.stream()
                .map(skillMapper::skillToSkillDTO)
                .collect(Collectors.toList());
    }
}
