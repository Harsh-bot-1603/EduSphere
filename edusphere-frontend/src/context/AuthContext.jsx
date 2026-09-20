import { createContext, useContext, useEffect, useMemo, useState, useCallback } from 'react';
import { decodeJwt, isExpired } from '../utils/jwt';
import { getToken, setToken, clearToken } from '../api/client';
import * as authApi from '../api/auth';

const AuthContext = createContext(null);

const NAME_KEY = 'edusphere_display_name';
const TEACHER_ID_KEY = 'edusphere_teacher_id';

function buildUser(token) {
  const payload = decodeJwt(token);
  if (!payload || isExpired(payload)) return null;
  return {
    email: payload.sub,
    role: payload.role, // "STUDENT" | "INSTRUCTOR" | "ADMIN"
    exp: payload.exp,
  };
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const token = getToken();
    const parsed = token ? buildUser(token) : null;
    if (token && !parsed) clearToken(); // stale/expired token left over from a previous visit
    return parsed;
  });
  const [displayName, setDisplayName] = useState(() => localStorage.getItem(NAME_KEY) || '');
  const [teacherId, setTeacherId] = useState(() => localStorage.getItem(TEACHER_ID_KEY) || null);

  // Passive expiry check - logs out once the token's exp claim has passed
  // while the tab is open, instead of only at the next request.
  useEffect(() => {
    if (!user?.exp) return;
    const msLeft = user.exp * 1000 - Date.now();
    if (msLeft <= 0) return;
    const timer = setTimeout(() => {
      clearToken();
      setUser(null);
    }, msLeft);
    return () => clearTimeout(timer);
  }, [user]);

  const login = useCallback(async (email, password) => {
    const res = await authApi.login({ email, password });
    setToken(res.token);
    setUser(buildUser(res.token));
    return buildUser(res.token);
  }, []);

  const register = useCallback(async ({ name, email, password, role }) => {
    const res = await authApi.register({ name, email, password, role });
    setToken(res.token);
    setUser(buildUser(res.token));
    localStorage.setItem(NAME_KEY, name);
    setDisplayName(name);
    return buildUser(res.token);
  }, []);

  const logout = useCallback(() => {
    clearToken();
    setUser(null);
  }, []);

  const rememberTeacherId = useCallback((id) => {
    if (id === undefined || id === null) return;
    localStorage.setItem(TEACHER_ID_KEY, String(id));
    setTeacherId(String(id));
  }, []);

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: !!user,
      role: user?.role || null,
      email: user?.email || null,
      displayName,
      teacherId,
      rememberTeacherId,
      login,
      register,
      logout,
    }),
    [user, displayName, teacherId, rememberTeacherId, login, register, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used inside <AuthProvider>');
  return ctx;
}
