package com.marcapo.template.service;

import com.marcapo.template.documents.User;
import com.marcapo.template.dto.LoginRequest;
import com.marcapo.template.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = new JwtUtils();
    }

    public String authenticateUser(LoginRequest loginRequest) throws Exception {
        User user;
        if (loginRequest.getEmail() != null && !loginRequest.getEmail().isEmpty()) {
            user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new Exception("Email not found"));
        } else if (loginRequest.getUsername() != null && !loginRequest.getUsername().isEmpty()) {
            user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new Exception("Username not found"));
        } else {
            throw new Exception("Username or email is required");
        }
        if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return jwtUtils.generateToken(user);
        } else {
            throw new Exception("Wrong password");
        }
    }
}
