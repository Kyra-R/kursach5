package com.example.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDTO {
    private String login;
    private String password;
}
