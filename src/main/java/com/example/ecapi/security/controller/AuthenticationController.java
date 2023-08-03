package com.example.ecapi.security.controller;

import com.example.ecapi.controller.AuthApi;
import com.example.ecapi.model.DTOAuthentication;
import com.example.ecapi.model.DTOLogin;
import com.example.ecapi.model.FormAuthenticate;
import com.example.ecapi.model.FormRegister;
import com.example.ecapi.security.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController implements AuthApi {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<DTOAuthentication> authRegister(FormRegister formRegister) {
        return ResponseEntity.ok(service.register(formRegister));
    }

    @Override
    public ResponseEntity<DTOLogin> login(FormAuthenticate formAuthenticate) {
        return ResponseEntity.ok(service.authenticate(formAuthenticate));
    }
}
