export default function Spinner({ label = 'Loading' }) {
  return (
    <div className="spinner-wrap" role="status" aria-label={label}>
      <span className="spinner" aria-hidden="true">
        <span className="spinner__body" />
        <span className="spinner__satellite" />
      </span>
      <p>{label}…</p>
    </div>
  );
}
