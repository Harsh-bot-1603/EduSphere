package com.edusphere.edusphere.entity;

import com.edusphere.edusphere.enums.EnrollmentStatus;
import jakarta.persistence.*;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User student;
    @ManyToOne
    private Course course;
    private String instructorName;
    private LocalDateTime enrollmentDate;
    @PositiveOrZero
    private int progress;
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
}
