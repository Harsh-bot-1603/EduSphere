package com.edusphere.edusphere.dto.response;

import com.edusphere.edusphere.enums.EnrollmentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long id;
    private String CourseTitle;
    private String instructorName;
    private LocalDateTime enrollmentDate;
    private int progress;
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
}
