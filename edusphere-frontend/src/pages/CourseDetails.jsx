import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import apiClient from "../apiClient";
import { Link } from "react-router-dom";

function CourseDetails() {
    const { courseId } = useParams();

    const [course, setCourse] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [lessons, setLessons] = useState([]);
    const [reviews, setReviews] = useState([]);
    const [rating, setRating] = useState(5);
    const [comment, setComment] = useState("");
    const [reviewMessage, setReviewMessage] = useState("");

    const handleReviewSubmit = async (e) => {
        e.preventDefault();

        try {
            const newReview = await apiClient(
                `/reviews/${courseId}/review`,
                {
                    method: "POST",
                    body: JSON.stringify({
                        rating: Number(rating),
                        comment: comment
                    })
                }
            );

            setReviews([...reviews, newReview]);
            setComment("");
            setRating(5);
            setReviewMessage("Review submitted successfully!");
        } catch (err) {
            setReviewMessage(err.message);
        }
    };
    useEffect(() => {
        const loadCourse = async () => {
            try {
                const courseData = await apiClient(`/courses/${courseId}`);
                setCourse(courseData);

                const lessonData = await apiClient(
                    `/lessons/courses/${courseId}/lessons`
                );
                setLessons(lessonData);
                const reviewData = await apiClient(
                    `/reviews/course/${courseId}`
                );

                setReviews(reviewData);

            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        loadCourse();
    }, [courseId]);

    if (loading) {
        return <p>Loading course...</p>;
    }

    if (error) {
        return <p>Error: {error}</p>;
    }

    if (!course) {
        return <p>Course not found.</p>;
    }

    return (

        <div>
            <Link to="/courses">← Back to Courses</Link>
            <h2>{course.title}</h2>

            <p><strong>Course ID:</strong> {course.id}</p>

            <p>{course.description}</p>

            <p>
                <strong>Instructor:</strong> {course.teacherName}
            </p>

            <p>
                <strong>Price:</strong> ₹{course.price}
            </p>

            <p>
                <strong>Status:</strong> {course.status}
            </p>
            <h3>Lessons</h3>

            {lessons.length === 0 ? (
                <p>No lessons available.</p>
            ) : (
                lessons.map((lesson) => (
                    <div key={lesson.id}>
                        <h4>
                            <Link to={`/lessons/${lesson.id}`}>
                                {lesson.title}
                            </Link>
                        </h4>
                    </div>
                ))
            )}
            <h3>Reviews</h3>

            {reviews.length === 0 ? (
                <p>No reviews yet.</p>
            ) : (
                reviews.map((review) => (
                    <div key={review.id}>
                        <p>Rating: {review.rating}/5</p>
                        <p>{review.comment}</p>
                    </div>
                ))
            )}
            <h3>Write a Review</h3>

            <form onSubmit={handleReviewSubmit}>
                <select
                    value={rating}
                    onChange={(e) => setRating(e.target.value)}
                >
                    <option value="5">5 - Excellent</option>
                    <option value="4">4 - Good</option>
                    <option value="3">3 - Average</option>
                    <option value="2">2 - Poor</option>
                    <option value="1">1 - Very Poor</option>
                </select>

                <br />

                <textarea
                    placeholder="Write your review..."
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    required
                />

                <br />

                <button type="submit">Submit Review</button>
            </form>

            {reviewMessage && <p>{reviewMessage}</p>}
        </div>
    );
}

export default CourseDetails;