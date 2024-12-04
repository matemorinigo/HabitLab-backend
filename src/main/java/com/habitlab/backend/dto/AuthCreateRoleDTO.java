package com.habitlab.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AuthCreateRoleDTO {
    @Size(max=3, message = "User can only have 3 roles")
    private List<String> roleListName;
}
