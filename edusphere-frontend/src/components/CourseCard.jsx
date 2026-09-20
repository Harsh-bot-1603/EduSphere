import { Link } from 'react-router-dom';
import { CourseStatusBadge } from './Badges';

const currency = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 });

export default function CourseCard({ course, mine = false }) {
  return (
    <Link to={`/courses/${course.id}`} className="course-card">
      <div className="course-card__top">
        <CourseStatusBadge status={course.status} />
        {mine && <span className="badge badge--mine">Your course</span>}
      </div>
      <h3>{course.title}</h3>
      <p className="course-card__desc">{course.description || 'No description yet.'}</p>
      <div className="course-card__foot">
        <span className="course-card__teacher">{course.teacherName}</span>
        <span className="course-card__price">{currency.format(course.price)}</span>
      </div>
    </Link>
  );
}
