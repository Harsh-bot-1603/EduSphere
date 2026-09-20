import { apiRequest } from './client';

// Note: the backend's field is called "username" on both requests, but it
// is actually used as the user's email (User.getUsername() returns email -
// see CustomUserDetailsService/AuthenticationService on the backend). We
// collect it as "email" in the UI and send it through as "username".
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
