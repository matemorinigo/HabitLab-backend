package com.habitlab.backend.config;

import com.habitlab.backend.persistance.entity.PermissionEntity;
import com.habitlab.backend.persistance.entity.RoleEntity;
import com.habitlab.backend.persistance.entity.RoleEnum;
import com.habitlab.backend.repository.PermissionRepository;
import com.habitlab.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializerConfig {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Bean
    CommandLineRunner init() {
        return args -> initializeAll();
    }

    @Transactional
    public void initializeAll() {
        createRoleIfNotExists(RoleEnum.ADMIN);
        createRoleIfNotExists(RoleEnum.GUEST);
        createRoleIfNotExists(RoleEnum.USER);

        createPermissionIfNotExists("CREATE");
        createPermissionIfNotExists("READ");
        createPermissionIfNotExists("UPDATE");
        createPermissionIfNotExists("DELETE");
    }

    private void createRoleIfNotExists(RoleEnum role) {
        if (roleRepository.findByRole(role) == null) {
            roleRepository.save(RoleEntity.builder()
                    .role(role)
                    .build());
        }
    }

    private void createPermissionIfNotExists(String permission) {
        if (permissionRepository.findByName(permission) == null) {
            permissionRepository.save(PermissionEntity.builder()
                    .name(permission)
                    .build());
        }
    }
}
