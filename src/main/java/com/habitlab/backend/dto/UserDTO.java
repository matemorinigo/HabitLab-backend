package com.habitlab.backend.dto;

import com.habitlab.backend.persistance.entity.HabitEntity;
import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private Set<HabitEntity> habits;
}
