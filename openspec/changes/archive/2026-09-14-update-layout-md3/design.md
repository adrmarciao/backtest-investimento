## Context

The current frontend layout uses custom CSS styles (`App.css`, `index.css`, `Sidebar.css`) that implement a glassmorphism theme. To align with the project guidelines in `.agents/agents/frontend.md` and the `material-3` skill, we need to introduce MD3 tokens and update layout components.

## Goals / Non-Goals

**Goals:**
- Implement Google Material Design 3 CSS custom properties (`--md-sys-color-*`, `--md-sys-shape-*`, MD3 typescale).
- Transform `Sidebar` into an MD3 Navigation Drawer / Navigation Rail with active pill indicators and smooth transitions.
- Restructure `App.jsx` layout shell to use MD3 Top App Bar and tonal surface background.
- Refactor existing cards, buttons, tabs, and tables to adopt MD3 surface tokens.

**Non-Goals:**
- Backend code modifications or changes to API endpoints.
- Introducing external web component libraries (e.g. `@material/web` NPM dependency) if vanilla CSS custom properties are sufficient for React integration.

## Decisions

### Decision 1: Pure CSS MD3 Custom Properties vs `@material/web` Web Components
- **Choice**: Use standard CSS custom properties (`--md-sys-color-*`, `--md-sys-shape-*`) and custom React CSS classes for MD3 component styling.
- **Rationale**: Web components from `@material/web` can add unnecessary bundle overhead and shadow DOM complexity to pure React JSX components. Standard CSS custom properties offer full control, light theme/dark theme support, and match `.agents/agents/frontend.md`.
- **Alternatives Considered**: `@material/web` custom elements.

### Decision 2: Surface Elevation Model
- **Choice**: Use MD3 surface container tokens (`--md-sys-color-surface-container-low`, `--md-sys-color-surface-container`, etc.) instead of CSS `backdrop-filter: blur()` and dark glass gradients.
- **Rationale**: Tonal surface elevation is the core principle of Material Design 3 for visual hierarchy and readability.

## Risks / Trade-offs

- [Risk] Existing custom CSS overrides might conflict during token migration. → **Mitigation**: Systematically update `index.css` root variables first and test layout components incrementally.
