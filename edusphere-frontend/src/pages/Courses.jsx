import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import apiClient from "../apiClient";
import "./Courses.css";

function Courses() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [enrolledCourses, setEnrolledCourses] = useState([]);

    const handleEnroll = async (courseId) => {
        try {
            await apiClient(`/enrollments/${courseId}/enroll`, {
                method: "POST"
            });

            setEnrolledCourses([...enrolledCourses, courseId]);

            alert("Successfully enrolled!");
        } catch (err) {
            alert(err.message);
        }
    };

    useEffect(() => {
        const loadData = async () => {
            try {
                const coursesData = await apiClient("/courses");

                setCourses(coursesData.content || []);

                const enrollmentsData = await apiClient(
                    "/enrollments/students/me/courses"
                );

                setEnrolledCourses(
                    enrollmentsData.map((enrollment) => enrollment.id)
                );

            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        loadData();
    }, []);

    if (loading) {
        return <p>Loading courses...</p>;
    }

    if (error) {
        return <p>Error: {error}</p>;
    }

    return (
        <div className="course-container">

            <h2>Available Courses</h2>

            {courses.length === 0 ? (
                <p>No courses available.</p>
            ) : (
                <div className="course-grid">

                    {courses.map((course) => (

                        <div className="course-card" key={course.id}>

                            <h3>{course.title}</h3>

                            <p>
                                {course.description}
                            </p>

                            <p>
                                <strong>Instructor:</strong>{" "}
                                {course.teacherName}
                            </p>

                            <p>
                                <strong>Price:</strong>{" "}
                                ₹{course.price}
                            </p>

                            <p>
                                <strong>Status:</strong>{" "}
                                {course.status}
                            </p>

                            <div className="course-actions">

                                <Link to={`/courses/${course.id}`}>
                                    <button>
                                        View Details
                                    </button>
                                </Link>

                                {enrolledCourses.includes(course.id) ? (

                                    <button disabled>
                                        Enrolled
                                    </button>

                                ) : (

                                    <button
                                        onClick={() =>
                                            handleEnroll(course.id)
                                        }
                                    >
                                        Enroll
                                    </button>

                                )}

                            </div>

                        </div>

                    ))}

                </div>
            )}

        </div>
    );
}

export default Courses;

