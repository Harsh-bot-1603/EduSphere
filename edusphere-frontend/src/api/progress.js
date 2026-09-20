import { apiRequest } from './client';

export function completeLesson(lessonId) {
  return apiRequest(`/progress/${lessonId}/complete`, { method: 'POST' });
}
