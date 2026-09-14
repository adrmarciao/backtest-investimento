# Design: Migração Visual para Material Design 3 (MUI)

## Architecture Overview

```
┌──────────────────────────────────────────────────────────────┐
│                      React Application                       │
│  ┌────────────────────────────────────────────────────────┐  │
│  │ ThemeProvider (MUI M3 Palette, Typography, Components) │  │
│  │  ┌──────────────────────────────────────────────────┐  │  │
│  │  │ CssBaseline (Global Normalization & Reset)       │  │  │
│  │  │  ┌────────────────────────────────────────────┐  │  │  │
│  │  │  │ Layout (AppBar, Sidebar / Drawer, Main)    │  │  │  │
│  │  │  │  ┌──────────────────────────────────────┐  │  │  │  │
│  │  │  │  │ Pages & Widgets (MUI Form / Cards)   │  │  │  │  │
│  │  │  │  └──────────────────────────────────────┘  │  │  │  │
│  │  │  └────────────────────────────────────────────┘  │  │  │
│  │  └──────────────────────────────────────────────────┘  │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

## Technical Choices
1. **MUI (`@mui/material`)**: Escolhido por ser o padrão de mercado para React, oferecendo suporte robusto a temas, acessibilidade out-of-the-box e integração fluida com React 19.
2. **Icons (`@mui/icons-material`)**: Para ícones vetoriais consistentes na navegação e ações.
3. **Theme Customization (`createTheme`)**: Definição de cores primárias, secundárias, superfícies e tipografia no padrão Material Design 3.

## Migration Strategy
- Instalação de dependências no pacote `frontend`.
- Criação de `src/theme/theme.js` exportando o tema customizado M3.
- Envolvimento do `App` com `ThemeProvider` e `CssBaseline`.
- Substituição progressiva das tags HTML brutas por componentes MUI (`Box`, `Grid`, `Card`, `Button`, `TextField`, `Typography`, `Drawer`).
