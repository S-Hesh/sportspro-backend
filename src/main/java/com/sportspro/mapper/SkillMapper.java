package com.sportspro.mapper;

import com.sportspro.dto.SkillDTO;
import com.sportspro.model.Skill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillDTO skillToSkillDTO(Skill skill);

    Skill skillDTOToSkill(SkillDTO skillDTO);
}
