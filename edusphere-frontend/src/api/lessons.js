import { apiRequest } from './client';

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
