package com.edusphere.edusphere.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDashBoardResponse {
    private long totalCourses;
    private long completedCourses;
    private long activeCourses;
    private int overallProgress;
}
