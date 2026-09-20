import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import apiClient from "../apiClient";

function LessonDetails() {
    const { lessonId } = useParams();

    const [lesson, setLesson] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadLesson = async () => {
            try {
                const data = await apiClient(`/lessons/courses/${lessonId}`);
                setLesson(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        loadLesson();
    }, [lessonId]);

    if (loading) {
        return <p>Loading lesson...</p>;
    }

    if (error) {
        return <p>Error: {error}</p>;
    }

    if (!lesson) {
        return <p>Lesson not found.</p>;
    }

    return (
        <div>
            <h2>{lesson.title}</h2>

            <p>{lesson.content}</p>
        </div>
    );
}

export default LessonDetails;