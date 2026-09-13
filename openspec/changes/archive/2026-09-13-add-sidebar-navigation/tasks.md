## 1. Sidebar Component Implementation

- [x] 1.1 Create `Sidebar.jsx` and `Sidebar.css` in `frontend/src/components/Sidebar/` with categorized navigation links ("Simulador & Estratégia", "Dados de Mercado") and verify UI rendering.
- [x] 1.2 Implement collapsible state (`isCollapsed`) with toggle button and `localStorage` persistence in `Sidebar.jsx`, verifying state persists on browser reload.
- [x] 1.3 Add inline SVG icons for menu items and category headers, ensuring visual alignment with dark glassmorphism design tokens.

## 2. Layout Integration & Refactoring

- [x] 2.1 Update `App.jsx` layout to flex row structure integrating `<Sidebar />` alongside `<main className="main-content">` and verify tab switching functions for all 4 views.
- [x] 2.2 Refactor `index.css` and `App.css` layout classes (`.app-container`, `.main-content`) for smooth flex sizing, overflow handling, and transition animations.
- [x] 2.3 Add responsive drawer/hamburger navigation styling for screens below 768px and verify layout responsiveness.

## 3. Verification & Build Validation

- [x] 3.1 Run frontend build (`npm run build` in `frontend/`) to verify JSX compilation and asset bundling without errors.

