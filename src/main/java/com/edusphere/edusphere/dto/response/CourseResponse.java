package com.edusphere.edusphere.dto.response;

import com.edusphere.edusphere.enums.CourseStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    Long id;
    String title;
    String description;
    BigDecimal price;
    Long teacherId;
    String teacherName;
    CourseStatus status;
}
