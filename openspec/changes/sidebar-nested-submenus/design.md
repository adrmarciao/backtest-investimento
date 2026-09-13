## Context

A Sidebar atual no React possui categorias renderizadas estaticamente. Precisamos transformar essa estrutura em um Accordion dinâmico onde apenas 1 menu principal esteja expandido por vez (Single Accordion), mantendo submenus recuados com ícones/rótulos e garantindo acessibilidade na sidebar compacta.

## Goals / Non-Goals

**Goals:**
- Implementar estado local no `Sidebar.jsx` (ex: `openMenuId`) para controlar qual menu principal está expandido.
- Abrir automaticamente o menu pai que contém a aba ativa (`activeTab`) na inicialização.
- Garantir comportamento de Accordion único (expandir um menu recolhe os demais).
- Adicionar estilos CSS e transições suaves para expansão/recolhimento e rotação de ícones (chevrons).
- Suportar visualização de submenus no modo recolhido (`isCollapsed`) via popover/flyout ao passar o mouse.

**Non-Goals:**
- Não incluir menus de Relatórios ou Configurações nesta fase.
- Não modificar rotas de navegação principal em `App.jsx` além do estado `activeTab`.

## Decisions

### 1. Estado de Menu Único (Single Accordion)
- **Decisão**: Armazenar o `openMenuId` (string/null) no estado do componente. Clicar em um menu principal alterna o ID (ou define `null` se já estiver aberto).
- **Alternativa Considerada**: Permitir múltiplos menus abertos simultaneamente. Descartado conforme opção escolhida pelo usuário.

### 2. Sincronização de Menu Ativo
- **Decisão**: Ao carregar o componente, identificar a categoria pai correspondente à `activeTab` e definir essa categoria como o `openMenuId` inicial.

### 3. Popover em Sidebar Compacta
- **Decisão**: Quando a barra lateral estiver recolhida (`isCollapsed === true`), o menu pai exibirá um popover flutuante alinhado à direita com a lista de submenus clicáveis.

## Risks / Trade-offs

- [Animação de expansão em CSS] → Usar transições em `max-height` e `opacity` com `overflow: hidden` para evitar pulos de layout.
