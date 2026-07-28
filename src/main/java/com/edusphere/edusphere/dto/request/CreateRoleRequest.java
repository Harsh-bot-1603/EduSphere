package com.edusphere.edusphere.dto.request;

import com.edusphere.edusphere.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequest {
    @NotNull(message = "Role name is required")
    private RoleType name;
}
