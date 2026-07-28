package com.edusphere.edusphere.repository;

import com.edusphere.edusphere.entity.Role;
import com.edusphere.edusphere.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    boolean existsByName(RoleType name);

    Optional<Role> findByName(RoleType roleType);
}
