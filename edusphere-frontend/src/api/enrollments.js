import { apiRequest } from './client';

export function enrollInCourse(courseId) {
  return apiRequest(`/enrollments/${courseId}/enroll`, { method: 'POST' });
}

export function getMyEnrollments() {
  return apiRequest('/enrollments/students/me/courses');
}

export function unenroll(enrollmentId) {
  return apiRequest(`/enrollments/${enrollmentId}`, { method: 'DELETE' });
}
