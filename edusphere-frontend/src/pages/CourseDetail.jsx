import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getCourse, updateCourse, deleteCourse } from '../api/courses';
import { listLessonsByCourse } from '../api/lessons';
import { listReviewsByCourse } from '../api/reviews';
import { enrollInCourse, unenroll, getMyEnrollments } from '../api/enrollments';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../context/ToastContext';
import { CourseStatusBadge } from '../components/Badges';
import Spinner from '../components/Spinner';
import EmptyState from '../components/EmptyState';
import ConfirmButton from '../components/ConfirmButton';
import CourseFormModal from '../components/CourseFormModal';
import LessonList from '../components/LessonList';
import ReviewsSection from '../components/ReviewsSection';
import ProgressRing from '../components/ProgressRing';

const currency = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 });

export default function CourseDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated, role, displayName, teacherId, rememberTeacherId } = useAuth();
  const toast = useToast();

  const [course, setCourse] = useState(null);
  const [lessons, setLessons] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [enrollment, setEnrollment] = useState(null); // { id, progress, status } for this course, if enrolled
  const [completedIds, setCompletedIds] = useState(() => new Set());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editingCourse, setEditingCourse] = useState(false);
  const [enrolling, setEnrolling] = useState(false);
  const [savingCourse, setSavingCourse] = useState(false);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError('');

    const calls = [getCourse(id), listLessonsByCourse(id), listReviewsByCourse(id)];
    if (isAuthenticated && role === 'STUDENT') calls.push(getMyEnrollments());

    Promise.all(calls)
      .then(([courseRes, lessonsRes, reviewsRes, enrollmentsRes]) => {
        if (cancelled) return;
        setCourse(courseRes);
        setLessons(lessonsRes);
        setReviews(reviewsRes);
        if (enrollmentsRes) {
          const mine = enrollmentsRes.find((e) => e.courseTitle === courseRes.title);
          setEnrollment(mine || null);
        }
      })
      .catch((err) => !cancelled && setError(err.message))
      .finally(() => !cancelled && setLoading(false));

    return () => {
      cancelled = true;
    };
  }, [id, isAuthenticated, role]);

  const canManage =
    isAuthenticated &&
    (role === 'ADMIN' ||
      (role === 'INSTRUCTOR' &&
        ((teacherId && course && String(course.teacherId) === String(teacherId)) ||
          (displayName && course && course.teacherName === displayName) ||
          
          (!teacherId && !displayName))));

  async function handleEnroll() {
    setEnrolling(true);
    try {
      const res = await enrollInCourse(course.id);
      setEnrollment(res);
      toast.success(`Enrolled in "${course.title}".`);
    } catch (err) {
      toast.error(err.message);
    } finally {
      setEnrolling(false);
    }
  }

  async function handleUnenroll() {
    try {
      await unenroll(enrollment.id);
      setEnrollment(null);
      setCompletedIds(new Set());
      toast.success('Unenrolled.');
    } catch (err) {
      toast.error(err.message);
    }
  }

  async function handleCourseUpdate(payload) {
    setSavingCourse(true);
    try {
      const updated = await updateCourse(course.id, payload);
      setCourse(updated);
      toast.success('Course updated.');
      setEditingCourse(false);
    } catch (err) {
      toast.error(err.message);
      throw err;
    } finally {
      setSavingCourse(false);
    }
  }

  async function handleCourseDelete() {
    try {
      await deleteCourse(course.id);
      toast.success(`Deleted "${course.title}".`);
      navigate('/');
    } catch (err) {
      toast.error(err.message);
    }
  }

  function handleLessonCompleted(lessonId) {
    setCompletedIds((prev) => new Set(prev).add(lessonId));
    getMyEnrollments()
      .then((list) => {
        const mine = list.find((e) => e.courseTitle === course.title);
        if (mine) setEnrollment(mine);
      })
      .catch(() => {});
  }

  if (loading) return <Spinner label="Loading course" />;
  if (error) return <EmptyState title="Couldn't load this course" hint={error} />;
  if (!course) return null;

  const isEnrolled = !!enrollment;

  return (
    <div className="page">
      <div className="course-detail">
        <div className="course-detail__main">
          <div className="course-detail__head">
            <CourseStatusBadge status={course.status} />
            <h1>{course.title}</h1>
            <p className="course-detail__teacher">Taught by {course.teacherName}</p>
          </div>

          <p className="course-detail__desc">{course.description || 'No description provided.'}</p>

          {canManage && (
            <div className="course-detail__manage">
              <button className="btn btn--ghost" onClick={() => setEditingCourse(true)}>
                Edit course
              </button>
              <ConfirmButton
                label="Delete course"
                confirmLabel="Really delete this course?"
                className="btn btn--ghost btn--danger"
                onConfirm={handleCourseDelete}
              />
              <span className="course-detail__manage-hint">
                Actions here are enforced by the backend — you'll get an error if you don't actually own this
                course.
              </span>
            </div>
          )}

          <section className="course-detail__section">
            <h2>Lessons</h2>
            <LessonList
              courseId={course.id}
              lessons={lessons}
              setLessons={setLessons}
              canManage={canManage}
              isEnrolled={isEnrolled}
              completedIds={completedIds}
              onLessonCompleted={handleLessonCompleted}
            />
          </section>

          <section className="course-detail__section">
            <ReviewsSection
              courseId={course.id}
              reviews={reviews}
              setReviews={setReviews}
              canReview={isAuthenticated && role === 'STUDENT' && isEnrolled}
              myName={displayName}
            />
          </section>
        </div>

        <aside className="course-detail__side">
          <div className="price-card">
            <span className="price-card__amount">{currency.format(course.price)}</span>

            {!isAuthenticated && (
              <>
                <button className="btn btn--primary btn--block" onClick={() => navigate('/login', { state: { from: { pathname: `/courses/${course.id}` } } })}>
                  Log in to enroll
                </button>
                <p className="price-card__hint">Only students can enroll in courses.</p>
              </>
            )}

            {isAuthenticated && role === 'STUDENT' && !isEnrolled && (
              <button className="btn btn--primary btn--block" onClick={handleEnroll} disabled={enrolling}>
                {enrolling ? 'Enrolling…' : 'Enroll now'}
              </button>
            )}

            {isAuthenticated && role === 'STUDENT' && isEnrolled && (
              <div className="price-card__progress">
                <ProgressRing percent={enrollment.progress} size={72} />
                <p>You're enrolled — {enrollment.status.toLowerCase()}</p>
                <ConfirmButton
                  label="Unenroll"
                  confirmLabel="Really unenroll?"
                  className="btn btn--ghost btn--danger btn--block"
                  onConfirm={handleUnenroll}
                />
              </div>
            )}

            {isAuthenticated && role !== 'STUDENT' && (
              <p className="price-card__hint">Enrollment is only available to student accounts.</p>
            )}

            <dl className="price-card__facts">
              <div>
                <dt>Lessons</dt>
                <dd>{lessons.length}</dd>
              </div>
              <div>
                <dt>Total time</dt>
                <dd>{lessons.reduce((sum, l) => sum + l.durationInMinutes, 0)} min</dd>
              </div>
            </dl>
          </div>
        </aside>
      </div>

      {editingCourse && (
        <CourseFormModal
          initial={course}
          submitting={savingCourse}
          onSubmit={handleCourseUpdate}
          onClose={() => setEditingCourse(false)}
        />
      )}
    </div>
  );
}
