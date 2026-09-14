## Why

The current web interface relies on custom glassmorphism styles and non-standard CSS variables that deviate from the project's official frontend design system guidelines defined in `.agents/agents/frontend.md` and the `material-3` skill. Updating the layout to Google Material Design 3 (Material You) establishes a unified, modern, adaptive design system with standardized color tokens, tonal elevation, shape scales, and responsive navigation patterns.

## What Changes

- **MD3 Color & Token System**: Replace custom CSS variables with standard Material Design 3 design tokens (`--md-sys-color-surface`, `--md-sys-color-primary`, `--md-sys-color-surface-container`, `--md-sys-shape-corner-*`, and MD3 typescale).
- **Adaptive Layout Architecture**: Modernize the app shell and navigation to align with MD3 Canonical Layouts (Navigation Drawer for wide screens, Navigation Rail for medium screens, and Navigation Bar for compact screens).
- **Tonal Surface Styling**: Replace dark glassmorphism gradients with MD3 tonal surfaces and elevation containers.
- **Component Modernization**: Update buttons, cards, table containers, and tab elements to use MD3 shape corner tokens and semantic color pairings.

## Capabilities

### New Capabilities

- `md3-design-system`: Defines the core Material Design 3 theme tokens, typography scale, shape scales, and tonal surface system for the web application.

### Modified Capabilities

- `app-navigation`: Updating navigation layout and styling rules to follow MD3 Navigation Drawer, Rail, and Navigation Bar specifications with pill-shaped active indicators and MD3 tonal containers.

## Impact

- **Frontend CSS**: `frontend/src/index.css` and `frontend/src/App.css` will be refactored to define and consume MD3 tokens.
- **Frontend App Shell**: `frontend/src/App.jsx` and `frontend/src/components/Sidebar/Sidebar.jsx` and `Sidebar.css` will be updated to implement the MD3 adaptive layout shell.
- **Components**: UI components in `frontend/src/components` will adopt MD3 tonal cards and button styling.
