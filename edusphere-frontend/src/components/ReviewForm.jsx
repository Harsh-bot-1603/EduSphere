import { useState } from 'react';
import StarRating from './StarRating';

export default function ReviewForm({ initial, onSubmit, onCancel, submitting }) {
  const [rating, setRating] = useState(initial?.rating || 0);
  const [comment, setComment] = useState(initial?.comment || '');
  const [error, setError] = useState('');

  async function handleSubmit(e) {
    e.preventDefault();
    if (!rating) {
      setError('Pick a star rating before submitting.');
      return;
    }
    setError('');
    await onSubmit({ rating, comment });
  }

  return (
    <form className="review-form" onSubmit={handleSubmit}>
      <StarRating value={rating} onChange={setRating} size={26} />
      <textarea
        placeholder="What did you think of this course? (optional)"
        value={comment}
        onChange={(e) => setComment(e.target.value)}
        rows={3}
        maxLength={2000}
      />
      {error && <span className="field-error">{error}</span>}
      <div className="form__actions">
        {onCancel && (
          <button type="button" className="btn btn--ghost" onClick={onCancel}>
            Cancel
          </button>
        )}
        <button type="submit" className="btn btn--primary" disabled={submitting}>
          {submitting ? 'Posting…' : initial ? 'Update review' : 'Post review'}
        </button>
      </div>
    </form>
  );
}
