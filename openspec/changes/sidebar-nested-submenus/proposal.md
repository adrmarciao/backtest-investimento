## Why

O menu lateral atual utiliza agrupamentos estáticos de itens. Para melhorar a usabilidade e preparar o sistema para futuras expansões sem poluição visual, a navegação lateral precisa suportar menus principais expansíveis com submenus interativos (estilo Accordion).

## What Changes

- Transformar as categorias do menu lateral em Menus Principais interativos (expansíveis/colapsáveis).
- Organizar as opções do simulador como submenus do menu principal "Simulador & Estratégia":
  - Motor de Backtest (Submenu)
  - Estratégia de Compra (Submenu)
- Organizar as opções de dados de mercado como submenus do menu principal "Dados de Mercado":
  - Indicadores Anuais (Submenu)
  - Cadastrar Ativos (Submenu)
- Implementar comportamento de Accordion (apenas um menu principal aberto por vez; expandir um recolhe os demais).
- Suportar navegação por flyout/popover quando a sidebar estiver no modo recolhido (compacto).

## Capabilities

### New Capabilities

### Modified Capabilities
- `app-navigation`: Atualiza a especificação de navegação para incluir a hierarquia de menu e submenu expansível (accordion).

## Impact

- Frontend (`frontend/src/components/Sidebar/Sidebar.jsx`, `Sidebar.css`).
- Sem impacto nas APIs do backend ou no esquema do banco de dados.
