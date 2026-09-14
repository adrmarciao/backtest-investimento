# Proposal: Migração do Frontend para Material Design 3 (MUI)

## Why
Atualmente, o frontend da aplicação possui estilos básicos e componentes customizados sem uma biblioteca de design padronizada. A adoção do Material Design 3 (M3) através do `@mui/material` garantirá uma interface moderna, responsiva, acessível e com componentes de UI prontos para produção.

## What
- Integração da biblioteca `@mui/material`, `@mui/icons-material`, `@emotion/react` e `@emotion/styled`.
- Configuração do `ThemeProvider` centralizado com tokens de tema baseados no Material Design 3.
- Refatoração dos componentes principais (Layout, Sidebar, Tabelas, Cards de Resultados e Formulários de Configuração) para usarem componentes MUI.
- Adequação das fontes (Roboto/Inter) e ícones padrão do Material.

## Capabilities Affected
- `md3-design-system`: Atualização da especificação de componentes de UI para o novo padrão M3 com React 19 / MUI v6.

## Non-goals
- Modificar lógicas de backtest ou APIs do backend.
- Alterar bibliotecas de gráficos (`recharts`), apenas estilizar seus wrappers/containers.
