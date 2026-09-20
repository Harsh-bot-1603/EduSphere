package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.request.CreateReviewRequest;
import com.edusphere.edusphere.dto.response.ReviewResponse;
import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.Review;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.enums.RoleType;
import com.edusphere.edusphere.exception.*;
import com.edusphere.edusphere.repository.CourseRepository;
import com.edusphere.edusphere.repository.EnrollmentRepository;
import com.edusphere.edusphere.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private ReviewResponse mapToResponse(Review review){
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .courseTitle(review.getCourse().getTitle())
                .studentName(review.getStudent().getName())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    @Transactional
    public ReviewResponse createReview(Long courseId, CreateReviewRequest request){
        User loggedInUser = getCurrentUser();
        if(loggedInUser.getRole().getName()!= RoleType.STUDENT) throw new AnauthorisedReviewException("You cannot review the course.");
        Course course =  courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundException(courseId));
        if(!enrollmentRepository.existsByStudentAndCourse(loggedInUser,course)) throw new EnrollmentDoesnotExistsException("You are not enrolled.");
        if(reviewRepository.existsByStudentAndCourse(loggedInUser,course)) throw new ReviewAlreadyExistsException("You have already reviewed.");
        Review review = Review.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .student(loggedInUser)
                .course(course)
                .build();
        Review savedReview = reviewRepository.save(review);
        return mapToResponse(savedReview);
    }
    public List<ReviewResponse> getReviewsByCourse(Long courseId){
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundException(courseId));
        List<Review> reviews = reviewRepository.findByCourse(course);
        List<ReviewResponse> responses = new ArrayList<>();
        for(Review review : reviews){
            responses.add(mapToResponse(review));
        }
        return responses;
    }
    public ReviewResponse updateReview(CreateReviewRequest request,Long reviewId){
        User loggedInUser = getCurrentUser();
        Review review = reviewRepository.findById(reviewId).orElseThrow(()-> new ReviewNotFoundException("Review does not exists."));
        if(!review.getStudent().getId().equals(loggedInUser.getId()) && loggedInUser.getRole().getName()!=RoleType.ADMIN)
            throw new UnauthorizedUpdateException("You cannot update the review.");
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        Review savedReview = reviewRepository.save(review);
        return mapToResponse(savedReview);
    }
    public void deleteReview(Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new ReviewNotFoundException("Review does not exists"));
        User loggedInUser = getCurrentUser();
        if(!review.getStudent().getId().equals(loggedInUser.getId()) && loggedInUser.getRole().getName()!=RoleType.ADMIN)
            throw new UnauthorizedDeletionException("You cannot delete this review.");
        reviewRepository.delete(review);

    }

}
