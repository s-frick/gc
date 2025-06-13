import path from "path"
import tailwindcss from "@tailwindcss/vite"
import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",   // dein Backend
        changeOrigin: true,
      },
    },
  },
  test: {
    globals: true,
    environment: 'jsdom', // für DOM-Zugriff in Tests
    setupFiles: './vitest.setup.ts', // optional
  },
})
