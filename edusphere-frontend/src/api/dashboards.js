import { apiRequest } from './client';

export const getStudentDashboard = () => apiRequest('/students/me/dashboard');
export const getInstructorDashboard = () => apiRequest('/instructors/me/dashboard');
export const getAdminDashboard = () => apiRequest('/admin/dashboard');
