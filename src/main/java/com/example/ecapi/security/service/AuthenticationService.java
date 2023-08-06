package com.example.ecapi.security.service;

import com.example.ecapi.model.*;
import com.example.ecapi.security.*;
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

    public DTOLogin authenticate(FormAuthenticate request) {
        User user = repository.findByEmailAndRole(request.getEmail(), request.getRole())
                .orElseThrow();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getId().toString(),
                        request.getPassword()
                )
        );
        String jwtToken = jwtService.generateToken(user);

        DTOUser dtoUser = new DTOUser(
                user.getId(),user.getFirstname(),user.getLastname(),user.getEmail(),user.getRole()
        );
        return new DTOLogin(jwtToken, dtoUser);
    }

}
