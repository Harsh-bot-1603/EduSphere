package com.edusphere.edusphere.repository;

import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.Enrollment;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    boolean existsByStudentAndCourse(User student, Course course);

    List<Enrollment> findByStudent(User user);

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);


    long countByStudent(User student);

    long countByStudentAndStatus(User student, EnrollmentStatus status);
    @Query("""
    SELECT COUNT(DISTINCT e.student.id)
    FROM Enrollment e
    WHERE e.course.teacher = :teacher
""")
    long countDistinctStudentsByTeacher(@Param("teacher") User teacher);
}
