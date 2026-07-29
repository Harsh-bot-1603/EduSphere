package com.edusphere.edusphere.repository;

import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long> {
    List<Lesson> findByCourseOrderByLessonOrder(Course course);
    boolean existsByCourseAndLessonOrder(Course course,int lessonOrder);
}
