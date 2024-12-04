package com.habitlab.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRegisterUserRequestDTO {
    private String username;
    private String password;
    private String email;
}
