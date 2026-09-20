import { useState } from 'react';
import Modal from './Modal';

export default function LessonFormModal({ initial, nextOrder, onSubmit, onClose, submitting }) {
  const isEdit = !!initial;
  const [title, setTitle] = useState(initial?.title || '');
  const [description, setDescription] = useState(initial?.description || '');
  const [videoUrl, setVideoUrl] = useState(initial?.videoUrl || '');
  const [durationInMinutes, setDurationInMinutes] = useState(initial?.durationInMinutes || 10);
  const [lessonOrder, setLessonOrder] = useState(initial?.lessonOrder ?? nextOrder ?? 1);
  const [errors, setErrors] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    setErrors(null);
    try {
      await onSubmit({
        title,
        description,
        videoUrl,
        durationInMinutes: Number(durationInMinutes),
        lessonOrder: Number(lessonOrder),
      });
    } catch (err) {
      if (err.fieldErrors) setErrors(err.fieldErrors);
      else throw err;
    }
  }

  return (
    <Modal title={isEdit ? 'Edit lesson' : 'Add a lesson'} onClose={onClose}>
      <form className="form" onSubmit={handleSubmit}>
        <label>
          Title
          <input value={title} onChange={(e) => setTitle(e.target.value)} required maxLength={500} />
          {errors?.title && <span className="field-error">{errors.title}</span>}
        </label>
        <label>
          Description
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            rows={3}
            maxLength={2000}
          />
        </label>
        <label>
          Video URL
          <input
            value={videoUrl}
            onChange={(e) => setVideoUrl(e.target.value)}
            placeholder="https://…"
            maxLength={1000}
          />
        </label>
        <div className="form__row">
          <label>
            Duration (minutes)
            <input
              type="number"
              min="1"
              value={durationInMinutes}
              onChange={(e) => setDurationInMinutes(e.target.value)}
              required
            />
          </label>
          <label>
            Lesson order
            <input
              type="number"
              min="1"
              value={lessonOrder}
              onChange={(e) => setLessonOrder(e.target.value)}
              required
            />
            {errors?.lessonOrder && <span className="field-error">{errors.lessonOrder}</span>}
          </label>
        </div>
        <p className="form__hint">Order must be a unique number within this course - it decides display order.</p>
        <div className="form__actions">
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cancel
          </button>
          <button type="submit" className="btn btn--primary" disabled={submitting}>
            {submitting ? 'Saving…' : isEdit ? 'Save changes' : 'Add lesson'}
          </button>
        </div>
      </form>
    </Modal>
  );
}
