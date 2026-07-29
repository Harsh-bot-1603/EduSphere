package com.edusphere.edusphere.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=500)
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id",nullable = false)
    private Course course;
}
