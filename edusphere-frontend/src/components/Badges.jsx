export function CourseStatusBadge({ status }) {
  return <span className={`badge badge--status-${(status || '').toLowerCase()}`}>{status}</span>;
}

export function EnrollmentStatusBadge({ status }) {
  return <span className={`badge badge--enroll-${(status || '').toLowerCase()}`}>{status}</span>;
}

export function RoleBadge({ role }) {
  return <span className={`badge badge--role-${(role || '').toLowerCase()}`}>{role}</span>;
}
