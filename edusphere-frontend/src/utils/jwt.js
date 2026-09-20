// Minimal JWT payload decoder. EduSphere's tokens are signed HS256 JWTs
// with a "role" claim (see JwtService.generateToken on the backend), a
// "sub" claim holding the user's email, and standard "iat"/"exp" claims.
// We only ever need to read the payload on the client - verification
// happens on the server on every request.
export function decodeJwt(token) {
  if (!token) return null;
  const parts = token.split('.');
  if (parts.length !== 3) return null;
  try {
    const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    const padded = base64.padEnd(base64.length + ((4 - (base64.length % 4)) % 4), '=');
    const json = decodeURIComponent(
      atob(padded)
        .split('')
        .map((c) => '%' + c.charCodeAt(0).toString(16).padStart(2, '0'))
        .join('')
    );
    return JSON.parse(json);
  } catch {
    return null;
  }
}

export function isExpired(payload) {
  if (!payload?.exp) return false;
  return Date.now() >= payload.exp * 1000;
}
