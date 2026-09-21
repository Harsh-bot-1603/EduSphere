const BASE_URL = import.meta.env.VITE_API_BASE_URL;
const TOKEN_KEY = 'edusphere_token';

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}
export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}
export function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}

/**
 * ApiError carries the HTTP status and, where the backend sent one, the
 * per-field validation error map from ErrorResponse.errors.
 *
 * Heads up: most exceptions on the EduSphere backend (course not found,
 * unauthorised update, duplicate enrollment, etc.) are not mapped to a
 * specific @ExceptionHandler, so they fall through to the generic handler
 * and come back as HTTP 500 - not 404/403/409 - even though the JSON body
 * still has a correct, readable `message`. Because status codes aren't
 * reliable here, this client (and every page) reads `error.message` to
 * decide what to show the user rather than branching on `error.status`.
 */
export class ApiError extends Error {
  constructor(message, status, fieldErrors) {
    super(message);
    this.status = status;
    this.fieldErrors = fieldErrors || null;
  }
}

export async function apiRequest(path, { method = 'GET', body, auth = true, params={} } = {}) {
  const headers = { 'Content-Type': 'application/json' };
  const token = getToken();
  if (auth && token) headers.Authorization = `Bearer ${token}`;

  let url = `${BASE_URL}${path}`;
  if (params) {
    const qs = new URLSearchParams();
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') qs.append(key, value);
    });
    const qsString = qs.toString();
    if (qsString) url += `?${qsString}`;
  }

  let res;
  try {
    const token = getToken()
    const requestHeaders = {
      'Content-Type':'application/json',
      ...(auth && token?{'Authorization':`Bearer ${token}`}:{})
    };
    res = await fetch(url, {
      method,
      headers : requestHeaders,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
  } catch (error) {
    console.error(error)
    throw new ApiError(
      `Can't reach the EduSphere API at ${BASE_URL}. Check the server is running and that VITE_API_BASE_URL is set correctly.`,
      0
    );
  }

  if (res.status === 204) return null;

  let data = null;
  const text = await res.text();
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = null;
    }
  }

  if (!res.ok) {
    const message = data?.message || data?.error || `Request failed with status ${res.status}`;
    throw new ApiError(message, res.status, data?.errors);
  }

  return data;
}
