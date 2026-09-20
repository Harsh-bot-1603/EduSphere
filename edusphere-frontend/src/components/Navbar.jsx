import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { RoleBadge } from './Badges';

export default function Navbar() {
  const { isAuthenticated, role, email, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <header className="navbar">
      <div className="navbar__inner">
        <NavLink to="/" className="navbar__brand">
          <span className="navbar__brand-mark" aria-hidden="true">
            <span />
            <span />
          </span>
          EduSphere
        </NavLink>

        <nav className="navbar__links">
          <NavLink to="/" end>
            Courses
          </NavLink>
          {isAuthenticated && <NavLink to="/dashboard">Dashboard</NavLink>}
        </nav>

        <div className="navbar__auth">
          {isAuthenticated ? (
            <>
              <span className="navbar__user">
                <RoleBadge role={role} />
                <span className="navbar__email">{email}</span>
              </span>
              <button
                className="btn btn--ghost"
                onClick={() => {
                  logout();
                  navigate('/');
                }}
              >
                Log out
              </button>
            </>
          ) : (
            <>
              <NavLink to="/login" className="btn btn--ghost">
                Log in
              </NavLink>
              <NavLink to="/register" className="btn btn--primary">
                Sign up
              </NavLink>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
