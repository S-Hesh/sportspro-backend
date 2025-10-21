package com.sportspro.controller;

import com.sportspro.dto.NetworkUserDTO;
import com.sportspro.dto.UserDTO;
import com.sportspro.dto.UserProfileResponse;
import com.sportspro.model.Skill;
import com.sportspro.model.User;
import com.sportspro.service.FileStorageService;
import com.sportspro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileStorageService fileStorageService; // handle avatar files

    // User Registration
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    // User Login (For simplicity, we're returning a UserDTO based on email)
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserDTO userDTO) {
        UserDTO user = userService.getUserByEmail(userDTO.getEmail());
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    /* Update Profile
    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUserProfile(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserProfile(userDTO);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    } */

    //Get Authenticated User Profile for Frontend
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // extracted from JWT
        UserProfileResponse profile = userService.getCurrentUserProfile(email);
        return profile != null ? ResponseEntity.ok(profile) : ResponseEntity.notFound().build();
    }

    // UPDATE user profile with avatar + skills + bio etc.
    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUserProfile(
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("bio") String bio,
            @RequestParam("skills") List<String> skills,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();

        User user = userService.findEntityByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        //  Update basic fields
        user.setName(name);
        user.setLocation(location);
        user.setBio(bio);

        // Update avatar if new one provided
        if (avatar != null && !avatar.isEmpty()) {
            String savedFilename = fileStorageService.storeImage(avatar);
            user.setAvatar("/uploads/" + savedFilename); // matches public URL
        }

        // Replace skills (clear + add)
        user.getSkills().clear();
        for (String skillName : skills) {
            Skill skill = new Skill();
            skill.setSkillName(skillName);
            skill.setUser(user);
            user.getSkills().add(skill);
        }

        //  Save and return updated user
        User saved = userService.saveRaw(user);
        return ResponseEntity.ok(userService.toDTO(saved));
    }

    //Athlete fetcher
    @GetMapping("/networking")
    public ResponseEntity<List<NetworkUserDTO>> getNetworkingUsers(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<NetworkUserDTO> users = userService.getOtherUsersForNetworking(email);
        return ResponseEntity.ok(users);
    }



}
