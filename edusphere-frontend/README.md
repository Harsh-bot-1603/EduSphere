# EduSphere frontend

A React (Vite) frontend for your EduSphere Spring Boot backend — course catalog with search/sort/filter,
auth (JWT), role-based dashboards (student/instructor/admin), lesson management, lesson-completion
progress, and reviews.

## Setup

```bash
npm install
cp .env.example .env          # then edit VITE_API_BASE_URL to point at your Railway URL
npm run dev                   # http://localhost:5173
```

`npm run build` produces a static `dist/` folder you can deploy anywhere (Vercel, Netlify, Railway static
hosting, etc).

## Before this will actually work against Railway: fix your backend's CORS

`CorsConfig.java` currently only allows `http://localhost:5173` as an origin. That's fine for local dev
against a local backend, but if you run this frontend against your **Railway-deployed** backend from
anywhere other than `localhost:5173` — including this same frontend once *you* deploy it somewhere — every
request will be blocked by the browser. Update `setAllowedOrigins(...)` to include wherever this frontend
actually runs, then redeploy to Railway. This is not something the frontend can work around; it has to be
fixed server-side.

## Backend bugs I found and patched

While wiring this up I found two bugs in your backend that make basic features impossible through the API
as shipped. I've included patched copies of the three affected files in `backend-fixes/` (same package
path, ready to drop into your project) — **apply these and redeploy to Railway before testing lessons or
reviews.**

1. **`LessonResponse` never included the lesson's own `id`.** The list endpoint
   (`GET /lessons/courses/{courseId}/lessons`) is the only way the frontend has to discover a lesson, but
   without an `id` in the response there's no way to call edit/delete/complete on any lesson you got from
   that list. Fixed by adding `private Long id;` to `LessonResponse` and setting it in
   `LessonService.mapToLessonResponse()`.

2. **Same bug in `ReviewResponse`.** The field existed on the DTO but `ReviewService.mapToResponse()`
   never set `id`, `createdAt`, or `updatedAt` — every review came back with `id: null`. Fixed the same
   way.

Without these two fixes, "mark lesson complete", "edit/delete a lesson", and "edit/delete a review" cannot
work at all — this isn't a frontend workaround issue, the data just isn't there.

## Backend issues I did *not* fix (your call)

- **`CourseService.searchCourse()` ignores `minPrice`/`maxPrice`.** It builds a `Specification` with those
  filters and then never uses it — only title search actually runs server-side. The frontend works around
  this by fetching up to 100 matching results and filtering by price in the browser, with a note shown to
  the user when a price filter is active. Real fix: apply the `Specification` in the repository call.
- **Most business exceptions return HTTP 500 instead of 404/403/409.** `GlobalExceptionHandler` only maps
  a handful of exceptions specifically; things like `CourseNotFoundException`, `UnauthorizedUpdateException`,
  `EnrollmentExistsException`, etc. all fall through to the generic handler. The JSON body's `message` is
  still readable, so the frontend reads `error.message` everywhere instead of branching on status codes —
  but you'll want proper status codes for any other client (mobile app, third party) that expects them.
- **`UserAlreadyExistsException` never calls `super(message)`,** so registering with a duplicate email
  returns a 500 with a `null` message. The frontend shows a generic "that email may already be registered"
  message as a fallback.
- **Anyone can self-register as `ADMIN`.** `RegisterRequest.role` isn't restricted server-side. The
  sign-up form here still offers all three roles (Student/Instructor/Admin) so you can actually reach and
  test the admin dashboard — but you should lock this down before real users touch it.
- **No `/users/me` endpoint and no "list my courses" endpoint.** The JWT only carries the user's email and
  role, not their numeric id or name, so the frontend can't reliably know "which of these courses are
  mine" or "which review is mine" from an existing session. See below.
- **`EnrollmentResponse` doesn't include `courseId`,** so a student's dashboard can't link straight to the
  enrolled course. Worked around by linking to a title search instead (`/?q=<course title>`) — one extra
  click, but it doesn't risk sending someone to the wrong course.
- **There's no `PUT`/endpoint to publish a course.** New courses are always created as `DRAFT`, and neither
  `UpdateCourseRequest` nor `CourseService.updateCourse()` touches `status`. As shipped, there is no way to
  move a course to `PUBLISHED` through the API at all.
- **There's no endpoint to read which lessons a student has already completed** — only the write-only
  `POST /progress/{lessonId}/complete`. The frontend only knows a lesson is "done" for the rest of the
  current browser session (from your own click, or from the "already completed" error if you click twice);
  that state is lost on refresh. A `GET` for per-lesson progress would fix this properly.

## The "who am I" workaround

Because there's no `/users/me` endpoint, the frontend uses two local, best-effort tricks stored in
`localStorage`:

- The name you type when **registering** is remembered, and used to match `teacherName` / `studentName` in
  API responses against "you" (for badges like "Your course" and for finding "your review").
- The first time you **create a course**, the response includes your real `teacherId` — the app remembers
  that permanently, which is the one fully-reliable signal it has.

Neither of these is a security mechanism — every actual edit/delete is still checked by the backend against
your logged-in account, regardless of what the UI shows. They only affect which buttons get shown.

## Project structure

```
src/
  api/          fetch wrappers, one file per resource
  components/   reusable UI (cards, modals, badges, star rating, progress ring…)
  context/      AuthContext (JWT/session), ToastContext (notifications)
  pages/        one file per route
  utils/jwt.js  minimal JWT payload decoder (no external dependency)
```
