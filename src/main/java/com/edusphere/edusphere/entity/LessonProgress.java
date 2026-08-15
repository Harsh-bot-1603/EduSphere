package com.edusphere.edusphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "lesson_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"enrollment_id", "lesson_id"}
                )
        }
)
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id",nullable = false)
    private Enrollment enrollment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id",nullable = false)
    private Lesson lesson;
    @Column(nullable = false)
    private boolean completed = false;
    private LocalDateTime completedAt;

}
