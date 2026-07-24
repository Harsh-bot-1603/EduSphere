package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.dto.request.CreateRoleRequest;
import com.edusphere.edusphere.dto.response.RoleResponse;
import com.edusphere.edusphere.entity.Role;
import com.edusphere.edusphere.exception.RoleAlreadyExistsException;
import com.edusphere.edusphere.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request) throws RoleAlreadyExistsException {
        Role role = new Role();
        role.setName(request.getName());
        Role savedRole = roleService.createRole(role);

        RoleResponse response = new RoleResponse(savedRole.getId(),savedRole.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable Long id) throws RoleNotFoundException {
        return ResponseEntity.ok(roleService.findById(id));
    }
}
