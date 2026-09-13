## MODIFIED Requirements

### Requirement: Grouped Navigation Structure
The sidebar SHALL organize navigation items into hierarchical parent menus ("Simulador & Estratégia", "Dados de Mercado") containing expandable and collapsible submenus in a single-accordion pattern.

#### Scenario: Expand Parent Menu
- **WHEN** the user clicks on a parent menu header in the expanded sidebar
- **THEN** the parent menu SHALL toggle its submenus visibility, and automatically collapse any other currently open parent menu.

#### Scenario: Navigate via Submenu Item
- **WHEN** the user clicks on a submenu item under an expanded parent menu
- **THEN** the application SHALL switch the active view to display the selected module and highlight the active submenu item.

## ADDED Requirements

### Requirement: Collapsed Sidebar Submenu Access
The sidebar SHALL provide access to submenus via a flyout/popover menu when hovered in the collapsed (~64px) sidebar state.

#### Scenario: Hover Parent Menu in Collapsed State
- **WHEN** the user hovers over a parent menu icon while the sidebar is collapsed
- **THEN** a flyout popover menu SHALL appear displaying the submenus for direct selection.
