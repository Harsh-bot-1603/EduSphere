package com.edusphere.edusphere.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {
    private String title;
    @Size(max = 2000)
    private String description;
    @Positive
    private BigDecimal price;
}
