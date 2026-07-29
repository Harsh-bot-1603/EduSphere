package com.edusphere.edusphere.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLessonRequest {
    @NotBlank
    @Column(nullable = false)
    private String title;
    @Size(max = 2000)
    private String description;
    @Size(max=1000)
    private String videoUrl;
    @Positive
    @Column(nullable = false)
    private int durationInMinutes;
    @Positive
    @Column(nullable = false)
    private int lessonOrder;
}
