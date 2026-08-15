package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.dto.request.CreateReviewRequest;
import com.edusphere.edusphere.dto.response.ReviewResponse;
import com.edusphere.edusphere.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping("/{courseId}/review")
    public ResponseEntity<ReviewResponse> createReview(@PathVariable Long courseId,@RequestBody CreateReviewRequest request){
        ReviewResponse response = reviewService.createReview(courseId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByCourse(@PathVariable Long courseId){
        List<ReviewResponse> responses = reviewService.getReviewsByCourse(courseId);
        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{reviewId}/update")
    public ResponseEntity<ReviewResponse> updateReview(@RequestBody CreateReviewRequest request,@PathVariable Long reviewId){
        ReviewResponse response = reviewService.updateReview(request,reviewId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{reviewId}/delete")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
