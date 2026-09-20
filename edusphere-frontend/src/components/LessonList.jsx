import { useState } from 'react';
import ConfirmButton from './ConfirmButton';
import LessonFormModal from './LessonFormModal';
import * as lessonsApi from '../api/lessons';
import * as progressApi from '../api/progress';
import { useToast } from '../context/ToastContext';

export default function LessonList({
  courseId,
  lessons,
  setLessons,
  canManage,
  isEnrolled,
  completedIds,
  onLessonCompleted,
}) {
  const toast = useToast();
  const [modalMode, setModalMode] = useState(null); // null | 'create' | lesson object being edited
  const [busyId, setBusyId] = useState(null);
  const [saving, setSaving] = useState(false);

  const sorted = [...lessons].sort((a, b) => a.lessonOrder - b.lessonOrder);
  const nextOrder = sorted.length ? Math.max(...sorted.map((l) => l.lessonOrder)) + 1 : 1;

  async function handleComplete(lesson) {
    setBusyId(lesson.id);
    try {
      await progressApi.completeLesson(lesson.id);
      toast.success(`"${lesson.title}" marked complete.`);
      onLessonCompleted(lesson.id);
    } catch (err) {
      if (/already completed/i.test(err.message)) {
        toast(`You'd already completed "${lesson.title}".`);
        onLessonCompleted(lesson.id);
      } else {
        toast.error(err.message);
      }
    } finally {
      setBusyId(null);
    }
  }

  async function handleDelete(lesson) {
    setBusyId(lesson.id);
    try {
      await lessonsApi.deleteLesson(lesson.id);
      toast.success(`Deleted "${lesson.title}".`);
      setLessons((prev) => prev.filter((l) => l.id !== lesson.id));
    } catch (err) {
      toast.error(err.message);
    } finally {
      setBusyId(null);
    }
  }

  async function handleCreate(payload) {
    setSaving(true);
    try {
      const created = await lessonsApi.createLesson(courseId, payload);
      setLessons((prev) => [...prev, created]);
      toast.success(`Added "${created.title}".`);
      setModalMode(null);
    } finally {
      setSaving(false);
    }
  }

  async function handleUpdate(lesson, payload) {
    setSaving(true);
    try {
      const updated = await lessonsApi.updateLesson(lesson.id, payload);
      setLessons((prev) => prev.map((l) => (l.id === lesson.id ? { ...updated, id: lesson.id } : l)));
      toast.success(`Updated "${updated.title}".`);
      setModalMode(null);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="lesson-list">
      {sorted.length === 0 && <p className="lesson-list__empty">This course doesn't have any lessons yet.</p>}

      <ol>
        {sorted.map((lesson) => {
          const done = completedIds.has(lesson.id);
          return (
            <li key={lesson.id} className={`lesson-row${done ? ' is-done' : ''}`}>
              <span className="lesson-row__order">{lesson.lessonOrder}</span>
              <div className="lesson-row__body">
                <h4>{lesson.title}</h4>
                {lesson.description && <p>{lesson.description}</p>}
                <div className="lesson-row__meta">
                  <span>{lesson.durationInMinutes} min</span>
                  {lesson.videoUrl && (
                    <a href={lesson.videoUrl} target="_blank" rel="noreferrer">
                      Watch video
                    </a>
                  )}
                </div>
              </div>
              <div className="lesson-row__actions">
                {isEnrolled && !canManage && (
                  <button
                    className={`btn ${done ? 'btn--done' : 'btn--primary'}`}
                    disabled={done || busyId === lesson.id}
                    onClick={() => handleComplete(lesson)}
                  >
                    {done ? 'Completed ✓' : busyId === lesson.id ? 'Saving…' : 'Mark complete'}
                  </button>
                )}
                {canManage && (
                  <>
                    <button className="btn btn--ghost" onClick={() => setModalMode(lesson)}>
                      Edit
                    </button>
                    <ConfirmButton
                      label="Delete"
                      confirmLabel="Really delete?"
                      className="btn btn--ghost btn--danger"
                      onConfirm={() => handleDelete(lesson)}
                    />
                  </>
                )}
              </div>
            </li>
          );
        })}
      </ol>

      {canManage && (
        <button className="btn btn--secondary" onClick={() => setModalMode('create')}>
          + Add lesson
        </button>
      )}

      {modalMode === 'create' && (
        <LessonFormModal nextOrder={nextOrder} onSubmit={handleCreate} onClose={() => setModalMode(null)} submitting={saving} />
      )}
      {modalMode && modalMode !== 'create' && (
        <LessonFormModal
          initial={modalMode}
          onSubmit={(payload) => handleUpdate(modalMode, payload)}
          onClose={() => setModalMode(null)}
          submitting={saving}
        />
      )}
    </div>
  );
}
