package com.edusphere.edusphere.dto.response;

import com.edusphere.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private Long id;
    private RoleType name;
}
