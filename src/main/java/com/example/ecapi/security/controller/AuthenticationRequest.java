package com.example.ecapi.security.controller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {

    private String email;
    String password;

}
