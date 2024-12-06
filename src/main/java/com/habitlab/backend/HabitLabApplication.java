package com.habitlab.backend;

import com.habitlab.backend.persistance.entity.PermissionEntity;
import com.habitlab.backend.persistance.entity.RoleEntity;
import com.habitlab.backend.persistance.entity.RoleEnum;
import com.habitlab.backend.repository.PermissionRepository;
import com.habitlab.backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class HabitLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitLabApplication.class, args);
	}

}
