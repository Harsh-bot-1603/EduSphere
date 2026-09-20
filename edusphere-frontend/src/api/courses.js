import { apiRequest } from './client';

export function listCourses({ page = 0, size = 12, sort, title, minPrice, maxPrice } = {}) {
  return apiRequest('/courses', {
    auth: false,
    params: { page, size, sort, title, minPrice, maxPrice },
  });
}

export function getCourse(id) {
  return apiRequest(`/courses/${id}`, { auth: false });
}

export function createCourse(payload) {
  return apiRequest('/courses', { method: 'POST', body: payload });
}

export function updateCourse(id, payload) {
  return apiRequest(`/courses/${id}`, { method: 'PUT', body: payload });
}

export function deleteCourse(id) {
  return apiRequest(`/courses/${id}`, { method: 'DELETE' });
}
