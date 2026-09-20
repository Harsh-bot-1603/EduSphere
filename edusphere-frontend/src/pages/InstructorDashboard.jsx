import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getInstructorDashboard } from '../api/dashboards';
import { listCourses, createCourse } from '../api/courses';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../context/ToastContext';
import CourseCard from '../components/CourseCard';
import CourseFormModal from '../components/CourseFormModal';
import Spinner from '../components/Spinner';
import EmptyState from '../components/EmptyState';

const CATALOG_SCAN_SIZE = 200;

export default function InstructorDashboard() {
  const { teacherId, displayName, rememberTeacherId } = useAuth();
  const toast = useToast();
  const navigate = useNavigate();

  const [stats, setStats] = useState(null);
  const [myCourses, setMyCourses] = useState(null);
  const [identified, setIdentified] = useState(true);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [creating, setCreating] = useState(false);
  const [saving, setSaving] = useState(false);

  function load() {
    setLoading(true);
    setError('');
    Promise.all([getInstructorDashboard(), listCourses({ page: 0, size: CATALOG_SCAN_SIZE })])
      .then(([dashboard, page]) => {
        setStats(dashboard);
        if (teacherId) {
          setMyCourses(page.content.filter((c) => String(c.teacherId) === String(teacherId)));
          setIdentified(true);
        } else if (displayName) {
          setMyCourses(page.content.filter((c) => c.teacherName === displayName));
          setIdentified(true);
        } else {
          setMyCourses(page.content);
          setIdentified(false);
        }
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }

  useEffect(load, [teacherId, displayName]);

  async function handleCreate(payload) {
    setSaving(true);
    try {
      const created = await createCourse(payload);
      rememberTeacherId(created.teacherId);
      toast.success(`Created "${created.title}" as a draft.`);
      setCreating(false);
      navigate(`/courses/${created.id}`);
    } finally {
      setSaving(false);
    }
  }

  if (loading) return <Spinner label="Loading your dashboard" />;
  if (error) return <EmptyState title="Couldn't load your dashboard" hint={error} />;

  return (
    <div className="page">
      <div className="page__head">
        <h1>Your teaching</h1>
        <button className="btn btn--primary" onClick={() => setCreating(true)}>
          + Create course
        </button>
      </div>

      <div className="stat-row">
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.totalCourses}</span>
          <span className="stat-tile__label">Courses</span>
        </div>
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.totalStudents}</span>
          <span className="stat-tile__label">Students</span>
        </div>
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.totalReviews}</span>
          <span className="stat-tile__label">Reviews</span>
        </div>
        <div className="stat-tile">
          <span className="stat-tile__value">{stats.averageRating ? stats.averageRating.toFixed(1) : '—'}</span>
          <span className="stat-tile__label">Avg. rating</span>
        </div>
      </div>

      <h2>Your courses</h2>
      {!identified && (
        <p className="filters__note">
          We can't yet tell which listed courses are yours — that link is only made once you create your first
          course here, or if we recognise the name you registered with. Showing every course below; the edit and
          delete actions on each one are still checked against your account by the backend.
        </p>
      )}

      {myCourses.length === 0 ? (
        <EmptyState title="You haven't created a course yet" hint="Start with your first draft — you can add lessons right after." />
      ) : (
        <div className="course-grid">
          {myCourses.map((course) => (
            <CourseCard key={course.id} course={course} mine={identified} />
          ))}
        </div>
      )}

      {creating && (
        <CourseFormModal onSubmit={handleCreate} onClose={() => setCreating(false)} submitting={saving} />
      )}
    </div>
  );
}
