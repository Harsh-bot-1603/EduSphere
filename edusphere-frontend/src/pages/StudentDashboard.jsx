import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getStudentDashboard } from '../api/dashboards';
import { getMyEnrollments, unenroll } from '../api/enrollments';
import { EnrollmentStatusBadge } from '../components/Badges';
import ProgressRing from '../components/ProgressRing';
import ConfirmButton from '../components/ConfirmButton';
import Spinner from '../components/Spinner';
import EmptyState from '../components/EmptyState';
import { useToast } from '../context/ToastContext';

export default function StudentDashboard() {
  const toast = useToast();
  const [stats, setStats] = useState(null);
  const [enrollments, setEnrollments] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  function load() {
    setLoading(true);
    setError('');
    Promise.all([getStudentDashboard(), getMyEnrollments()])
      .then(([dashboard, myEnrollments]) => {
        setStats(dashboard);
        setEnrollments(myEnrollments);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }

  useEffect(load, []);

  async function handleUnenroll(id) {
    try {
      await unenroll(id);
      toast.success('Unenrolled from the course.');
      setEnrollments((prev) => prev.filter((e) => e.id !== id));
    } catch (err) {
      toast.error(err.message);
    }
  }

  if (loading) return <Spinner label="Loading your dashboard" />;
  if (error) return <EmptyState title="Couldn't load your dashboard" hint={error} />;

  return (
    <div className="page">
      <h1>Your learning</h1>

      <div className="stat-row">
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.totalCourses}</span>
          <span className="stat-tile__label">Enrolled courses</span>
        </div>
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.activeCourses}</span>
          <span className="stat-tile__label">In progress</span>
        </div>
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.completedCourses}</span>
          <span className="stat-tile__label">Completed</span>
        </div>
        <div className="stat-tile stat-tile--ring">
          <ProgressRing percent={stats.overallProgress} size={64} />
          <span className="stat-tile__label">Overall progress</span>
        </div>
      </div>

      <h2>My courses</h2>
      {enrollments.length === 0 ? (
        <EmptyState
          title="You haven't enrolled in anything yet"
          hint="Browse the catalog and pick a course to get started."
          action={
            <Link to="/" className="btn btn--primary">
              Browse courses
            </Link>
          }
        />
      ) : (
        <ul className="enrollment-list">
          {enrollments.map((e) => (
            <li key={e.id} className="enrollment-row">
              <ProgressRing percent={e.progress} size={48} stroke={4} />
              <div className="enrollment-row__body">
                <h3>{e.courseTitle}</h3>
                <p>
                  Taught by {e.instructorName} · Enrolled{' '}
                  {new Date(e.enrollmentDate).toLocaleDateString()}
                </p>
              </div>
              <EnrollmentStatusBadge status={e.status} />
              <Link className="btn btn--ghost" to={`/?q=${encodeURIComponent(e.courseTitle)}`}>
                View course
              </Link>
              <ConfirmButton
                label="Unenroll"
                confirmLabel="Really unenroll?"
                className="btn btn--ghost btn--danger"
                onConfirm={() => handleUnenroll(e.id)}
              />
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
