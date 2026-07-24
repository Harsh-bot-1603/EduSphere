package com.edusphere.edusphere.service;

import com.edusphere.edusphere.entity.Role;
import com.edusphere.edusphere.exception.RoleAlreadyExistsException;
import com.edusphere.edusphere.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;

    }
    public Role createRole(Role role) throws RoleAlreadyExistsException {

        return roleRepository.save(role);

    }

    public Role findById(Long id) throws RoleNotFoundException {
         Optional<Role> role = roleRepository.findById(id);

            return role.orElseThrow(
                    () -> new RoleNotFoundException(id.toString())
            );

    }
}
