package com.habitlab.backend.repository;

import com.habitlab.backend.persistance.entity.RoleEntity;
import com.habitlab.backend.persistance.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findRoleEntitiesByRoleIn(Collection<RoleEnum> role);
    RoleEntity findRoleEntityByRole(RoleEnum role);

    RoleEntity findByRole(RoleEnum role);
}
