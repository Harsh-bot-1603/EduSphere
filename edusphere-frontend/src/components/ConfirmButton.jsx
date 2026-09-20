import { useEffect, useRef, useState } from 'react';

/**
 * A destructive action button that requires a second click to confirm,
 * instead of a native window.confirm() dialog. Reverts back to the
 * original label if left alone for a few seconds or if the user clicks
 * elsewhere.
 */
export default function ConfirmButton({ label, confirmLabel = 'Confirm?', onConfirm, className = '' }) {
  const [confirming, setConfirming] = useState(false);
  const timerRef = useRef(null);

  useEffect(() => () => clearTimeout(timerRef.current), []);

  useEffect(() => {
    if (!confirming) return;
    const cancel = () => setConfirming(false);
    document.addEventListener('click', cancel);
    return () => document.removeEventListener('click', cancel);
  }, [confirming]);

  return (
    <button
      type="button"
      className={`${className} ${confirming ? 'is-confirming' : ''}`}
      onClick={(e) => {
        e.stopPropagation();
        if (confirming) {
          clearTimeout(timerRef.current);
          setConfirming(false);
          onConfirm();
        } else {
          setConfirming(true);
          timerRef.current = setTimeout(() => setConfirming(false), 3000);
        }
      }}
    >
      {confirming ? confirmLabel : label}
    </button>
  );
}
