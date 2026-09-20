import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../context/ToastContext';

const ROLES = [
  { value: 'STUDENT', label: 'Student', hint: 'Enroll in courses and track your progress.' },
  { value: 'INSTRUCTOR', label: 'Instructor', hint: 'Create courses and lessons.' },
  { value: 'ADMIN', label: 'Admin', hint: 'View platform-wide totals.' },
];

export default function Register() {
  const { register } = useAuth();
  const toast = useToast();
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('STUDENT');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitting(true);
    setError('');
    try {
      await register({ name, email, password, role });
      toast.success(`Account created. You're signed in as a ${role.toLowerCase()}.`);
      navigate('/dashboard', { replace: true });
    } catch (err) {
      
      setError(err.message || 'That email may already be registered. Try logging in instead.');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="page page--narrow">
      <div className="auth-card">
        <h1>Create your account</h1>
        <p className="auth-card__sub">Pick the role that matches what you'll do on EduSphere.</p>
        <form className="form" onSubmit={handleSubmit}>
          <label>
            Full name
            <input value={name} onChange={(e) => setName(e.target.value)} required />
          </label>
          <label>
            Email
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              autoComplete="username"
            />
          </label>
          <label>
            Password
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              autoComplete="new-password"
            />
          </label>

          <fieldset className="role-picker">
            <legend>I am a…</legend>
            {ROLES.map((r) => (
              <label key={r.value} className={`role-option${role === r.value ? ' is-selected' : ''}`}>
                <input
                  type="radio"
                  name="role"
                  value={r.value}
                  checked={role === r.value}
                  onChange={() => setRole(r.value)}
                />
                <span className="role-option__label">{r.label}</span>
                <span className="role-option__hint">{r.hint}</span>
              </label>
            ))}
          </fieldset>

          {error && <p className="form__error">{error}</p>}
          <button type="submit" className="btn btn--primary btn--block" disabled={submitting}>
            {submitting ? 'Creating account…' : 'Create account'}
          </button>
        </form>
        <p className="auth-card__foot">
          Already have an account? <Link to="/login">Log in</Link>
        </p>
      </div>
    </div>
  );
}
