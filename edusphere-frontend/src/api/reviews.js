import { apiRequest } from './client';

export function listReviewsByCourse(courseId) {
  return apiRequest(`/reviews/course/${courseId}`, { auth: false });
}

export function createReview(courseId, payload) {
  return apiRequest(`/reviews/${courseId}/review`, { method: 'POST', body: payload });
}

export function updateReview(reviewId, payload) {
  return apiRequest(`/reviews/${reviewId}/update`, { method: 'PUT', body: payload });
}

export function deleteReview(reviewId) {
  return apiRequest(`/reviews/${reviewId}/delete`, { method: 'DELETE' });
}
