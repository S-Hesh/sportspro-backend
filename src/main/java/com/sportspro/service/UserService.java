package com.sportspro.service;

import com.sportspro.dto.NetworkUserDTO;
import com.sportspro.dto.UserDTO;
import com.sportspro.dto.UserProfileResponse;
import com.sportspro.mapper.UserMapper;
import com.sportspro.model.Skill;
import com.sportspro.model.User;
import com.sportspro.repository.ConnectionRepository;
import com.sportspro.repository.UserRepository;
import com.sportspro.util.ConnectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    // NEW: to compute relationship flags
    @Autowired
    private ConnectionService connectionService;

    // -------- Auth & Profile (unchanged behavior) --------

    public UserDTO registerUser(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        user = userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return userMapper.userToUserDTO(user);
        }
        return null;
    }

    public UserDTO updateUserProfile(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user != null) {
            user.setName(userDTO.getUsername());
            user.setBio(userDTO.getBio());
            user.setLocation(userDTO.getLocation());
            user.setAvatar(userDTO.getAvatar());
            user.setSport(userDTO.getSport());
            user = userRepository.save(user);
            return userMapper.userToUserDTO(user);
        }
        return null;
    }

    public User saveRaw(User u) { return userRepository.save(u); }
    public UserDTO toDTO(User u) { return userMapper.userToUserDTO(u); }
    public User findEntityByEmail(String email) { return userRepository.findByEmail(email); }

    public UserProfileResponse getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;

        UserProfileResponse profile = new UserProfileResponse();
        profile.setName(user.getName());
        profile.setAvatar(user.getAvatar());
        profile.setTitle(user.getSport());
        profile.setLocation(user.getLocation());
        profile.setJoined(user.getCreatedAt().format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        profile.setBio(user.getBio());

        if (user.getSkills() != null) {
            profile.setSkills(
                    user.getSkills().stream()
                            .map(Skill::getSkillName)
                            .collect(Collectors.toList())
            );
        } else {
            profile.setSkills(Collections.emptyList());
        }
        return profile;
    }

    // -------- Networking feed (enriched) --------

    public List<NetworkUserDTO> getOtherUsersForNetworking(String currentEmail) {
        User currentUser = userRepository.findByEmail(currentEmail);
        if (currentUser == null) return List.of();

        Long meId = currentUser.getUserId();
        List<User> others = userRepository.findByUserIdNot(meId);

        List<NetworkUserDTO> out = new ArrayList<>(others.size());
        for (User u : others) {
            List<String> skillNames = (u.getSkills() == null)
                    ? Collections.emptyList()
                    : u.getSkills().stream().map(Skill::getSkillName).collect(Collectors.toList());

            int connectionCount =
                    connectionRepository.findByUserUserIdAndStatus(u.getUserId(), ConnectionStatus.ACCEPTED).size()
                            + connectionRepository.findByConnectedUserUserIdAndStatus(u.getUserId(), ConnectionStatus.ACCEPTED).size();

            ConnectionService.RelationshipFlags f = connectionService.flags(meId, u.getUserId());

            out.add(NetworkUserDTO.builder()
                    .id(u.getUserId())
                    .name(u.getName())
                    .role(u.getSport())
                    .location(u.getLocation())
                    .avatar(u.getAvatar())
                    .skills(skillNames)
                    .connections(connectionCount)
                    // set the field named "connected", which is serialized as "isConnected"
                    .connected(f.isConnected)
                    .hasIncomingRequest(f.hasIncomingRequest)
                    .hasOutgoingRequest(f.hasOutgoingRequest)
                    .build()
            );
        }
        return out;
    }
}
