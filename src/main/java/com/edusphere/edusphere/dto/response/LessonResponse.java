package com.edusphere.edusphere.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonResponse {
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
    private Long courseId;
}
