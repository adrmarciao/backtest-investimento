## MODIFIED Requirements

### Requirement: Sidebar Navigation Layout
The frontend application SHALL display a left sidebar navigation panel that follows Material Design 3 Navigation Drawer / Rail specifications, allowing users to switch between active views and toggle between expanded (~240px Navigation Drawer) and collapsed (~80px Navigation Rail) layout states.

#### Scenario: Switching Active View
- **WHEN** the user clicks on a navigation item in the sidebar (e.g., "Motor de Backtest", "Estratégia de Compra", "Cadastrar Ativos", or "Indicadores Anuais")
- **THEN** the application SHALL switch the active view to display the selected module and highlight the active item using an MD3 pill-shaped active indicator (`--md-sys-color-secondary-container`).

#### Scenario: Collapsing and Expanding Sidebar
- **WHEN** the user clicks the sidebar collapse toggle button
- **THEN** the sidebar SHALL toggle between Navigation Drawer expanded state (showing icons and text labels) and Navigation Rail collapsed state (showing icons with labels centered below or tooltips), preserving space for the main content area.

### Requirement: Responsive Drawer Navigation
The application SHALL adapt sidebar navigation for smaller screen widths (mobile/tablet) by rendering a collapsible overlay drawer menu or MD3 Navigation Bar accessible via a hamburger icon.

#### Scenario: Mobile Screen Layout
- **WHEN** the viewport width is below 768px
- **THEN** the sidebar SHALL collapse into an off-canvas modal drawer or bottom Navigation Bar triggered by a top-bar hamburger toggle button, adhering to MD3 touch target guidelines.
