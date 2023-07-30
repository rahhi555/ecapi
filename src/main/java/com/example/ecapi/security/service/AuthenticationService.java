package com.example.ecapi.security.service;

import com.example.ecapi.model.DTOAuthentication;
import com.example.ecapi.model.EnumRole;
import com.example.ecapi.model.FormAuthenticate;
import com.example.ecapi.model.FormRegister;
import com.example.ecapi.security.*;
import com.example.ecapi.security.controller.AuthenticationRequest;
import com.example.ecapi.security.controller.AuthenticationResponse;
import com.example.ecapi.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public DTOAuthentication register(FormRegister request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        repository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return new DTOAuthentication(jwtToken);
    }

    public DTOAuthentication authenticate(FormAuthenticate request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return new DTOAuthentication(jwtToken);
    }

}
