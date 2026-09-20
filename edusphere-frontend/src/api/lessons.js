import { apiRequest } from './client';

// The backend's lesson routes are genuinely inconsistent - list/create use
// /lessons/courses/{courseId}, but get/update/delete use
// /lessons/courses/{lessonId} (the segment is still literally "courses"
// even though the id is a lesson id). These calls match the backend
// exactly as written.
export function listLessonsByCourse(courseId) {
  return apiRequest(`/lessons/courses/${courseId}/lessons`, { auth: false });
}

export function createLesson(courseId, payload) {
  return apiRequest(`/lessons/courses/${courseId}`, { method: 'POST', body: payload });
}

export function updateLesson(lessonId, payload) {
  return apiRequest(`/lessons/courses/${lessonId}`, { method: 'PUT', body: payload });
}

export function deleteLesson(lessonId) {
  return apiRequest(`/lessons/courses/${lessonId}`, { method: 'DELETE' });
}
