## Context

See proposal.md for motivation and scope.
Currently, `frontend/src/App.jsx` renders a single column container (`.app-container`) with a top header, horizontal button bar (`.tabs-nav`), and a main view container.

## Goals / Non-Goals

**Goals:**
- Replace top tab navigation with a collapsible left sidebar layout.
- Organize active tabs into logical categories ("Simulador & Estratégia", "Dados de Mercado").
- Ensure seamless visual transition matching the dark glassmorphism theme (`var(--bg-primary)`, `var(--accent-gradient)`).
- Preserve existing tab states (`assets`, `criteria`, `indicators`, `backtest`) and existing component behaviors.
- Ensure main workspace content resizes smoothly when collapsing/expanding the sidebar.

**Non-Goals:**
- Modifying backend APIs or database schemas.
- Adding complex nested multi-level routing (React Router) for now; maintaining state-driven tab switching (`activeTab`).

## Decisions

### Decision 1: Modular Sidebar Component (`frontend/src/components/Sidebar/`)
- Create `Sidebar.jsx` and `Sidebar.css` to encapsulate sidebar header (Logo + Title), nav sections, toggle button, and footer status.
- **Rationale**: Isolates navigation presentation and collapse state from main view rendering logic.

### Decision 2: Flexbox App Layout Structure
- Change `.app-container` layout from single column to a side-by-side flex layout (`display: flex; min-height: 100vh;`).
- Main content workspace will occupy `flex: 1` with `min-width: 0` to prevent chart/table flex overflow.

### Decision 3: Lightweight SVG Icons
- Use clean, inline SVG icons for menu items and categories.
- **Rationale**: Avoids adding new npm icon library dependencies while maintaining high crispness and exact color matching with CSS variables.

### Decision 4: Collapse State Persistence
- Store sidebar collapse state (`isCollapsed`) in `localStorage` so user preference is remembered across page reloads.

## Risks / Trade-offs

- **[Risk] Chart and Table Overflow**: Recharts and wide data tables (`PurchasesTable.jsx`, `IndicatorsTab.jsx`) might cause horizontal window scrolling if flex child bounds are unconstrained.
  - **Mitigation**: Apply `min-width: 0` and `overflow-x: auto` on the `<main className="main-content">` wrapper element.
- **[Risk] Small Screen Responsiveness**: Fixed 240px sidebar on narrow screens reduces workspace area.
  - **Mitigation**: Use media queries (`@media (max-width: 768px)`) to collapse sidebar into a mobile drawer overlay.
