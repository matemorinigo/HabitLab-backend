package com.habitlab.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponseDTO {
    private String username;
    private String token;
}
