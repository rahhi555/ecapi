package com.example.ecapi.security.controller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;

}
