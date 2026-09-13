package com.edusphere.edusphere.config;

import com.edusphere.edusphere.entity.Role;
import com.edusphere.edusphere.enums.RoleType;
import com.edusphere.edusphere.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        if (!roleRepository.existsByName(RoleType.STUDENT)) {
            roleRepository.save(
                    Role.builder()
                            .name(RoleType.STUDENT)
                            .build()
            );
        }

        if (!roleRepository.existsByName(RoleType.INSTRUCTOR)) {
            roleRepository.save(
                    Role.builder()
                            .name(RoleType.INSTRUCTOR)
                            .build()
            );
        }

        if (!roleRepository.existsByName(RoleType.ADMIN)) {
            roleRepository.save(
                    Role.builder()
                            .name(RoleType.ADMIN)
                            .build()
            );
        }
    }
}