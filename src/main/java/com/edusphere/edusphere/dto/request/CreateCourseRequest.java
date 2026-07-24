package com.edusphere.edusphere.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {
    @NotBlank
    @Max(500)
    private String title;
    @Max(2000)
    private String description;
    @Positive
    @NotNull
    private BigDecimal price;
}
