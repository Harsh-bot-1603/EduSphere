package com.edusphere.edusphere.repository;

import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.Enrollment;
import com.edusphere.edusphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    boolean existsByStudentAndCourse(User student, Course course);

    List<Enrollment> findByStudent(User user);
}
