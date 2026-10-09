import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    host: true,
    proxy: {
      '/api/expenses': {
        target: process.env.VITE_EXPENSE_API_TARGET || 'http://localhost:8081',
        changeOrigin: true,
      },
      '/api': {
        target: process.env.VITE_API_TARGET || 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})
