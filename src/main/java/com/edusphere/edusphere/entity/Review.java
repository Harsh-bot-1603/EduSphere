package com.edusphere.edusphere.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id","student_id"})})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(1)
    @Max(5)
    private int rating;
    @Size(max=2000)
            @Column(length = 2000)
    String comment;
    @Column(nullable = false,updatable = false)
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "student_id",nullable = false)
    User student;
    @ManyToOne
            @JoinColumn(name = "course_id",nullable = false)
    Course course;
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
