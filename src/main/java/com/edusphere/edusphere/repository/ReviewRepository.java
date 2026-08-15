package com.edusphere.edusphere.repository;

import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.Review;
import com.edusphere.edusphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByStudentAndCourse(User student, Course course);

    List<Review> findByCourse(Course course);

    boolean existsByStudentAndCourse(User student,Course course);
}
