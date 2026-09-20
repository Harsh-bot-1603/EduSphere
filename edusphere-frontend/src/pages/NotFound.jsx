import { Link } from 'react-router-dom';

export default function NotFound() {
  return (
    <div className="page page--narrow">
      <div className="empty-state">
        <h3>Page not found</h3>
        <p>That page doesn't exist, or the link is out of date.</p>
        <Link to="/" className="btn btn--primary">
          Back to courses
        </Link>
      </div>
    </div>
  );
}
