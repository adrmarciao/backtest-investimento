## Why

As the Value Investing Backtest system expands with new options (such as portfolio comparative analysis, data source status, and data import/export tools), the existing top horizontal tab bar has reached its layout capacity. Moving navigation to a collapsible left sidebar provides a scalable structure for expanding features, improves content readability by dedicating full screen width to charts and tables, and provides a modern layout experience.

## What Changes

- **Sidebar Component**: Create a reusable `Sidebar` navigation component with collapsible mode (expanded ~240px vs collapsed ~64px).
- **Navigation Organization**: Group current tabs (1. Cadastrar Ativos, 2. Estratégia de Compra, 3. Indicadores Anuais, 4. Motor de Backtest) into logical categorized sections ("Dados de Mercado", "Simulador & Estratégia") with icons and clear labels.
- **Main Layout Structure**: Refactor `App.jsx` and global CSS to use a side-by-side flex layout (Sidebar + Main Workspace Area).
- **Responsive Layout**: Support mobile drawer/hamburger menu overlay for smaller screen sizes.

## Capabilities

### New Capabilities
- `app-navigation`: Core sidebar navigation, section grouping, collapse toggle state, and active view switching for the frontend application.

### Modified Capabilities

## Impact

- **Frontend**: `App.jsx`, `App.css`, `index.css`, and addition of new components under `frontend/src/components/Sidebar/`.
- **APIs & Backend**: No breaking changes or impacts on backend endpoints.
