package com.edusphere.edusphere.dto.response;

import com.edusphere.edusphere.entity.Role;
import com.edusphere.edusphere.enums.RoleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private Long id;
    private RoleType name;
}
