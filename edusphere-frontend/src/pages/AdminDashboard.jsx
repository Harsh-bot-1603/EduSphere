import { useEffect, useState } from 'react';
import { getAdminDashboard } from '../api/dashboards';
import Spinner from '../components/Spinner';
import EmptyState from '../components/EmptyState';

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    getAdminDashboard()
      .then(setStats)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Spinner label="Loading platform stats" />;
  if (error) return <EmptyState title="Couldn't load the admin dashboard" hint={error} />;

  return (
    <div className="page">
      <h1>Platform overview</h1>
      <div className="stat-row">
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.totalUsers}</span>
          <span className="stat-tile__label">Users</span>
        </div>
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.totalCourses}</span>
          <span className="stat-tile__label">Courses</span>
        </div>
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.totalEnrollments}</span>
          <span className="stat-tile__label">Enrollments</span>
        </div>
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.totalReviews}</span>
          <span className="stat-tile__label">Reviews</span>
        </div>
      </div>
      <p className="filters__note">
        This account can self-manage any course from its detail page (edit or delete), since the backend treats
        ADMIN as an override on ownership checks. Browse the catalog from the Courses tab to find one.
      </p>
    </div>
  );
}
