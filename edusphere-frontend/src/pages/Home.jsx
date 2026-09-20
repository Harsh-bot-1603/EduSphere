import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { listCourses } from '../api/courses';
import CourseCard from '../components/CourseCard';
import Pagination from '../components/Pagination';
import Spinner from '../components/Spinner';
import EmptyState from '../components/EmptyState';
import { useAuth } from '../context/AuthContext';

const PAGE_SIZE = 12;
const PRICE_FILTER_SIZE = 100;

const SORTS = [
  { value: '', label: 'Newest listed' },
  { value: 'title,asc', label: 'Title, A to Z' },
  { value: 'title,desc', label: 'Title, Z to A' },
  { value: 'price,asc', label: 'Price, low to high' },
  { value: 'price,desc', label: 'Price, high to low' },
];

export default function Home() {
  const { teacherId, displayName } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();

  const title = searchParams.get('q') || '';
  const minPrice = searchParams.get('min') || '';
  const maxPrice = searchParams.get('max') || '';
  const sort = searchParams.get('sort') || '';
  const page = Number(searchParams.get('page') || 0);

  const [titleInput, setTitleInput] = useState(title);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const priceFilterActive = minPrice !== '' || maxPrice !== '';

  function updateParams(patch) {
    const next = new URLSearchParams(searchParams);
    Object.entries(patch).forEach(([key, value]) => {
      if (value === '' || value === undefined || value === null) next.delete(key);
      else next.set(key, value);
    });
    setSearchParams(next);
  }

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError('');

    listCourses({
      page: priceFilterActive ? 0 : page,
      size: priceFilterActive ? PRICE_FILTER_SIZE : PAGE_SIZE,
      sort: sort || undefined,
      title: title || undefined,
    })
      .then((res) => !cancelled && setData(res))
      .catch((err) => !cancelled && setError(err.message))
      .finally(() => !cancelled && setLoading(false));

    return () => {
      cancelled = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [title, sort, page, priceFilterActive]);

  useEffect(() => {
    setTitleInput(title);
  }, [title]);

  const min = minPrice === '' ? -Infinity : Number(minPrice);
  const max = maxPrice === '' ? Infinity : Number(maxPrice);
  const visibleCourses = (data?.content || []).filter((c) => Number(c.price) >= min && Number(c.price) <= max);

  return (
    <div className="page">
      <section className="hero">
        <div className="hero__orbit" aria-hidden="true">
          <span className="hero__ring hero__ring--1" />
          <span className="hero__ring hero__ring--2" />
          <span className="hero__ring hero__ring--3" />
          <span className="hero__core" />
        </div>
        <div className="hero__copy">
          <h1>Every course, one orbit.</h1>
          <p>
            Browse what's live on EduSphere, track your progress lesson by lesson, and pick up right where you
            left off.
          </p>
        </div>
      </section>

      <section className="filters">
        <form
          className="filters__search"
          onSubmit={(e) => {
            e.preventDefault();
            updateParams({ q: titleInput.trim(), page: undefined });
          }}
        >
          <input
            type="search"
            placeholder="Search courses by title…"
            value={titleInput}
            onChange={(e) => setTitleInput(e.target.value)}
          />
          <button type="submit" className="btn btn--primary">
            Search
          </button>
        </form>

        <div className="filters__row">
          <label className="filters__field">
            Min price
            <input
              type="number"
              min="0"
              placeholder="0"
              value={minPrice}
              onChange={(e) => updateParams({ min: e.target.value })}
            />
          </label>
          <label className="filters__field">
            Max price
            <input
              type="number"
              min="0"
              placeholder="Any"
              value={maxPrice}
              onChange={(e) => updateParams({ max: e.target.value })}
            />
          </label>
          <label className="filters__field">
            Sort by
            <select value={sort} onChange={(e) => updateParams({ sort: e.target.value, page: undefined })}>
              {SORTS.map((s) => (
                <option key={s.value} value={s.value}>
                  {s.label}
                </option>
              ))}
            </select>
          </label>
        </div>
        {priceFilterActive && (
          <p className="filters__note">
            Price filtering runs in your browser over the first {PRICE_FILTER_SIZE} matching results — the
            backend's price parameters aren't applied server-side. Combine with a title search to narrow things
            down further.
          </p>
        )}
      </section>

      {loading && <Spinner label="Loading courses" />}
      {!loading && error && <EmptyState title="Couldn't load courses" hint={error} />}

      {!loading && !error && visibleCourses.length === 0 && (
        <EmptyState title="No courses match your filters" hint="Try widening your price range or clearing the search." />
      )}

      {!loading && !error && visibleCourses.length > 0 && (
        <>
          <div className="course-grid">
            {visibleCourses.map((course) => (
              <CourseCard
                key={course.id}
                course={course}
                mine={
                  (teacherId && String(course.teacherId) === String(teacherId)) ||
                  (!!displayName && course.teacherName === displayName)
                }
              />
            ))}
          </div>
          {!priceFilterActive && data && (
            <Pagination page={data.number} totalPages={data.totalPages} onChange={(p) => updateParams({ page: p })} />
          )}
        </>
      )}
    </div>
  );
}
