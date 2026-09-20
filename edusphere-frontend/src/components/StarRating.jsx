import { useState } from 'react';

/**
 * Renders 1-5 stars. Pass `onChange` to make it interactive (a picker);
 * omit it to render a read-only rating display.
 */
export default function StarRating({ value = 0, onChange, size = 20 }) {
  const [hover, setHover] = useState(0);
  const interactive = typeof onChange === 'function';
  const active = hover || value;

  return (
    <div
      className={`star-rating${interactive ? ' star-rating--interactive' : ''}`}
      onMouseLeave={() => interactive && setHover(0)}
      role={interactive ? 'radiogroup' : 'img'}
      aria-label={interactive ? 'Choose a rating' : `Rated ${value} out of 5`}
    >
      {[1, 2, 3, 4, 5].map((n) => (
        <span
          key={n}
          className={`star${n <= active ? ' star--filled' : ''}`}
          style={{ fontSize: size }}
          onMouseEnter={() => interactive && setHover(n)}
          onClick={() => interactive && onChange(n)}
          role={interactive ? 'radio' : undefined}
          aria-checked={interactive ? n === value : undefined}
        >
          ★
        </span>
      ))}
    </div>
  );
}
