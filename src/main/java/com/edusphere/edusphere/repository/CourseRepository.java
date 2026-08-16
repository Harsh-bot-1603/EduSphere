package com.edusphere.edusphere.repository;

import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long>, JpaSpecificationExecutor<Course> {
    Page<Course> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<Course> findByTeacher(User teacher);

}
