import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// EduSphere frontend dev server runs on port 5173 by default (Vite's default).
// The backend's CorsConfig currently only allows http://localhost:5173 as an
// allowed origin - see README.md "Backend issues you need to fix" section.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
  },
});
