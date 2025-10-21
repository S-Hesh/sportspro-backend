
package com.sportspro.service;

import com.sportspro.dto.*;
import com.sportspro.mapper.UserMapper;
import com.sportspro.model.User;
import com.sportspro.repository.UserRepository;
import com.sportspro.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class AuthService {

    @Autowired private UserRepository userRepo;
    @Autowired private UserMapper userMapper;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.findByEmail(req.getEmail()) != null) {
            throw new RuntimeException("Email already in use");
        }
        User u = new User();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setSport(req.getSport());
        u.setPassword(encoder.encode(req.getPassword()));
        u = userRepo.save(u);

        UserDTO dto = userMapper.userToUserDTO(u);
        String token = jwtUtil.generateToken(u.getEmail());
        return new AuthResponse(token, dto);
    }

    public AuthResponse login(LoginRequest req) {
        User u = userRepo.findByEmail(req.getEmail());
        if (u == null || !encoder.matches(req.getPassword(), u.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        UserDTO dto = userMapper.userToUserDTO(u);
        String token = jwtUtil.generateToken(u.getEmail());
        return new AuthResponse(token, dto);
    }
}
