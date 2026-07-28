package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.request.CreateRoleRequest;
import com.edusphere.edusphere.dto.response.RoleResponse;
import com.edusphere.edusphere.entity.Role;
import com.edusphere.edusphere.exception.RoleAlreadyExistsException;
import com.edusphere.edusphere.exception.RoleNotFoundException;
import com.edusphere.edusphere.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;

    }
    public RoleResponse createRole(CreateRoleRequest request){
        if(roleRepository.existsByName(request.getName())) throw new RoleAlreadyExistsException("Role already exists");
        Role role = Role.builder()
                .name(request.getName())
                .build();
        Role savedRole = roleRepository.save(role);
        return new RoleResponse(savedRole.getId(),savedRole.getName());

    }

    public Role findById(Long id) throws RoleNotFoundException {
         Optional<Role> role = roleRepository.findById(id);

            return role.orElseThrow(
                    () -> new RoleNotFoundException(id.toString())
            );

    }
}
