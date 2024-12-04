package com.habitlab.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthLoginRequestDTO {
    private String username;
    private String password;
}
