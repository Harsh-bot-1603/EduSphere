import { useAuth } from '../context/AuthContext';
import StudentDashboard from './StudentDashboard';
import InstructorDashboard from './InstructorDashboard';
import AdminDashboard from './AdminDashboard';

export default function Dashboard() {
  const { role } = useAuth();

  if (role === 'INSTRUCTOR') return <InstructorDashboard />;
  if (role === 'ADMIN') return <AdminDashboard />;
  return <StudentDashboard />;
}
