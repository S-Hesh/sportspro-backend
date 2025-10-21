package com.sportspro.controller;

import com.sportspro.dto.SkillDTO;
import com.sportspro.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    // Add a new skill
    @PostMapping("/add")
    public ResponseEntity<SkillDTO> addSkill(@RequestBody SkillDTO skillDTO) {
        SkillDTO createdSkill = skillService.addSkill(skillDTO);
        return ResponseEntity.ok(createdSkill);
    }

    // Get skills by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SkillDTO>> getSkillsByUserId(@PathVariable Long userId) {
        List<SkillDTO> skills = skillService.getSkillsByUserId(userId);
        return ResponseEntity.ok(skills);
    }
}
