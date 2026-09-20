import { useToastList } from '../context/ToastContext';

export default function ToastStack() {
  const { toasts, dismiss } = useToastList();
  if (!toasts.length) return null;

  return (
    <div className="toast-stack" role="status" aria-live="polite">
      {toasts.map((t) => (
        <div key={t.id} className={`toast toast--${t.variant}`}>
          <span className="toast__dot" aria-hidden="true" />
          <p>{t.message}</p>
          <button className="toast__close" onClick={() => dismiss(t.id)} aria-label="Dismiss">
            ×
          </button>
        </div>
      ))}
    </div>
  );
}
