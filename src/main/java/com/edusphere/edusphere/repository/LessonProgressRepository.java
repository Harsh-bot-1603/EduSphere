package com.edusphere.edusphere.repository;

import com.edusphere.edusphere.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress,Long> {
    List<LessonProgress> findByEnrollment(Enrollment enrollment);

    Optional<LessonProgress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);

    long countByEnrollmentAndCompletedTrue(Enrollment enrollment);


}
