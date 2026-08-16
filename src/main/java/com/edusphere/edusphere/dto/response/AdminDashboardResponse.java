package com.edusphere.edusphere.dto.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponse {

    private long totalUsers;
    private long totalCourses;
    private long totalEnrollments;
    private long totalReviews;
}