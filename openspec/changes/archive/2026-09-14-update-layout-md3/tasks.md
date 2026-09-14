## 1. Material Design 3 Design System Setup

- [x] 1.1 Update `frontend/src/index.css` to define full MD3 color tokens (`--md-sys-color-*`), shape tokens (`--md-sys-shape-corner-*`), typography tokens, and surface container classes, and verify that styles parse cleanly.
- [x] 1.2 Replace custom button (`.btn`) and card (`.glass-card`) classes in `frontend/src/index.css` with MD3 compliant surface and component utility classes (`.md3-card`, `.md3-btn`, etc.), and verify styling definitions.

## 2. Navigation & App Shell Modernization

- [x] 2.1 Refactor `frontend/src/components/Sidebar/Sidebar.jsx` and `Sidebar.css` to follow MD3 Navigation Drawer and Navigation Rail layout standards, using pill-shaped active item indicators and MD3 tonal container backgrounds. Verify via visual rendering.
- [x] 2.2 Update `frontend/src/App.jsx` layout wrapper and header to implement the MD3 Top App Bar and main content surface shell. Verify page structure and responsiveness.

## 3. Tab Components & Surface Refactoring

- [x] 3.1 Update `BacktestTab.jsx`, `CriteriaTab.jsx`, `AssetsTab.jsx`, and `IndicatorsTab.jsx` card containers and controls to consume MD3 surface tokens and buttons. Verify that tab switching functions correctly.
- [x] 3.2 Verify the overall application build and responsive layout behavior using `npm run build` or Vite build check in `frontend/`.
