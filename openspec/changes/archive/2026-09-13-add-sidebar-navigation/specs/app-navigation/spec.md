## Purpose

Provides a centralized collapsible left sidebar navigation component for the Value Investing Backtest application to switch between application sections and support future module expansion.

## ADDED Requirements

### Requirement: Sidebar Navigation Layout
The frontend application SHALL display a left sidebar navigation panel that allows switching between active views and toggling between expanded (~240px) and collapsed (~64px) layout states.

#### Scenario: Switching Active View
- **WHEN** the user clicks on a navigation item in the sidebar (e.g., "Motor de Backtest", "Estratégia de Compra", "Cadastrar Ativos", or "Indicadores Anuais")
- **THEN** the application SHALL switch the active view to display the selected module and highlight the active item in the sidebar.

#### Scenario: Collapsing and Expanding Sidebar
- **WHEN** the user clicks the sidebar collapse toggle button
- **THEN** the sidebar SHALL toggle between expanded state (showing icons and text labels) and collapsed state (showing icons only with hover tooltips), preserving space for the main content area.

### Requirement: Grouped Navigation Structure
The sidebar SHALL organize navigation items into categorized groups ("Simulador & Estratégia", "Dados de Mercado") with icons and distinct group headers.

#### Scenario: Display Categorized Navigation Groups
- **WHEN** the sidebar is rendered in expanded state
- **THEN** navigation items SHALL be visually grouped under category headings with corresponding icons for each navigation item.

### Requirement: Responsive Drawer Navigation
The application SHALL adapt sidebar navigation for smaller screen widths (mobile/tablet) by rendering a collapsible overlay drawer menu accessible via a hamburger icon.

#### Scenario: Mobile Screen Layout
- **WHEN** the viewport width is below 768px
- **THEN** the sidebar SHALL collapse into an off-canvas drawer triggered by a top-bar hamburger toggle button.
