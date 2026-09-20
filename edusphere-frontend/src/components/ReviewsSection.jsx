import { useState } from 'react';
import StarRating from './StarRating';
import ReviewForm from './ReviewForm';
import ConfirmButton from './ConfirmButton';
import * as reviewsApi from '../api/reviews';
import { useToast } from '../context/ToastContext';

export default function ReviewsSection({ courseId, reviews, setReviews, canReview, myName }) {
  const toast = useToast();
  const [editing, setEditing] = useState(false);
  const [saving, setSaving] = useState(false);

  const average = reviews.length
    ? (reviews.reduce((sum, r) => sum + r.rating, 0) / reviews.length).toFixed(1)
    : null;

  // Best-effort: the API doesn't expose a reviewer id, only a display
  // name, so "my review" is a name match against the name you registered
  // with in this browser (see README for why).
  const myReview = myName ? reviews.find((r) => r.studentName === myName) : null;

  async function handlePost(payload) {
    setSaving(true);
    try {
      const created = await reviewsApi.createReview(courseId, payload);
      setReviews((prev) => [created, ...prev]);
      toast.success('Review posted.');
    } catch (err) {
      toast.error(err.message);
    } finally {
      setSaving(false);
    }
  }

  async function handleUpdate(payload) {
    setSaving(true);
    try {
      const updated = await reviewsApi.updateReview(myReview.id, payload);
      setReviews((prev) => prev.map((r) => (r.id === myReview.id ? { ...updated, id: myReview.id } : r)));
      toast.success('Review updated.');
      setEditing(false);
    } catch (err) {
      toast.error(err.message);
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    try {
      await reviewsApi.deleteReview(myReview.id);
      setReviews((prev) => prev.filter((r) => r.id !== myReview.id));
      toast.success('Review deleted.');
    } catch (err) {
      toast.error(err.message);
    }
  }

  return (
    <div className="reviews">
      <div className="reviews__head">
        <h2>Reviews</h2>
        {average && (
          <span className="reviews__average">
            <StarRating value={Math.round(average)} size={16} /> {average} · {reviews.length}{' '}
            {reviews.length === 1 ? 'review' : 'reviews'}
          </span>
        )}
      </div>

      {canReview && !myReview && (
        <div className="reviews__form-card">
          <h3>Write a review</h3>
          <ReviewForm onSubmit={handlePost} submitting={saving} />
        </div>
      )}

      {myReview && (
        <div className="reviews__form-card reviews__form-card--mine">
          <h3>Your review</h3>
          {editing ? (
            <ReviewForm initial={myReview} onSubmit={handleUpdate} onCancel={() => setEditing(false)} submitting={saving} />
          ) : (
            <>
              <StarRating value={myReview.rating} size={18} />
              {myReview.comment && <p>{myReview.comment}</p>}
              <div className="form__actions">
                <button className="btn btn--ghost" onClick={() => setEditing(true)}>
                  Edit
                </button>
                <ConfirmButton
                  label="Delete"
                  confirmLabel="Really delete?"
                  className="btn btn--ghost btn--danger"
                  onConfirm={handleDelete}
                />
              </div>
            </>
          )}
        </div>
      )}

      {reviews.length === 0 ? (
        <p className="reviews__empty">No reviews yet — be the first to leave one.</p>
      ) : (
        <ul className="review-list">
          {reviews
            .filter((r) => r !== myReview)
            .map((r) => (
              <li key={r.id ?? `${r.studentName}-${r.comment}`} className="review-item">
                <div className="review-item__head">
                  <strong>{r.studentName}</strong>
                  <StarRating value={r.rating} size={14} />
                </div>
                {r.comment && <p>{r.comment}</p>}
              </li>
            ))}
        </ul>
      )}
    </div>
  );
}
