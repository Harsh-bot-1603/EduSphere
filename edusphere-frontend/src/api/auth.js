import { apiRequest } from './client';

export function login({ email, password }) {
  return apiRequest('/auth/login', {
    method: 'POST',
    auth: false,
    body: { username: email, password },
  });
}

export function register({ name, email, password, role }) {
  return apiRequest('/auth/register', {
    method: 'POST',
    auth: false,
    body: { name, username: email, password, role },
  });
}
