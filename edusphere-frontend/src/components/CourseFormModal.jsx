import { useState } from 'react';
import Modal from './Modal';

export default function CourseFormModal({ initial, onSubmit, onClose, submitting }) {
  const isEdit = !!initial;
  const [title, setTitle] = useState(initial?.title || '');
  const [description, setDescription] = useState(initial?.description || '');
  const [price, setPrice] = useState(initial?.price ?? '');
  const [errors, setErrors] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    setErrors(null);
    try {
      await onSubmit({ title, description, price: Number(price) });
    } catch (err) {
      if (err.fieldErrors) setErrors(err.fieldErrors);
      else throw err;
    }
  }

  return (
    <Modal title={isEdit ? 'Edit course' : 'Create a course'} onClose={onClose}>
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
            rows={4}
            maxLength={2000}
          />
          {errors?.description && <span className="field-error">{errors.description}</span>}
        </label>
        <label>
          Price (₹)
          <input
            type="number"
            min="0"
            step="0.01"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            required
          />
          {errors?.price && <span className="field-error">{errors.price}</span>}
        </label>
        {!isEdit && (
          <p className="form__hint">New courses are created as DRAFT. The backend has no endpoint to publish a course - see the README.</p>
        )}
        <div className="form__actions">
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cancel
          </button>
          <button type="submit" className="btn btn--primary" disabled={submitting}>
            {submitting ? 'Saving…' : isEdit ? 'Save changes' : 'Create course'}
          </button>
        </div>
      </form>
    </Modal>
  );
}
