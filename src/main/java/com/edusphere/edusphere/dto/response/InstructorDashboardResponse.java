package com.edusphere.edusphere.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorDashboardResponse {
    private long totalCourses;
    private long totalStudents;
    private long totalReviews;
    private double averageRating;
}
