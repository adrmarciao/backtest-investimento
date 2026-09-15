## Why

Atualmente, o motor de backtest executa a simulação obrigatoriamente considerando todos os ativos cadastrados no sistema. Isso impede que o usuário faça simulações focadas em uma carteira específica ou teste hipóteses isoladas com um subconjunto de ativos. Permite ao usuário selecionar granularmente na interface os tickers a serem incluídos no backtest.

## What Changes

- **Seleção de Tickers no Frontend**: Inclusão de um componente Autocomplete (Material UI) multi-seleção com chips dos tickers selecionados na aba do motor de backtest, acompanhado de botões utilitários "Selecionar Todos" e "Limpar Seleção".
- **Comportamento Inicial**: Pré-seleção automática de todos os ativos cadastrados ao abrir a tela de backtest.
- **Validação de Interface**: Bloqueio de execução se nenhum ticker estiver selecionado.
- **Payload da API de Backtest**: Atualização do endpoint `POST /api/v1/backtest` para aceitar uma lista opcional/obrigatória de `tickers`.
- **Filtro de Ativos no Backend**: Adaptação do `ExecuteBacktestUseCase` para executar o backtest exclusivamente sobre os ativos cujo ticker consta na requisição.

## Capabilities

### New Capabilities
<!-- None -->

### Modified Capabilities
- `backtest-engine`: Atualização do requisito de execução de backtest para aceitar a seleção de tickers específicos, processando compras, dividendos, métricas e serie temporal com base no subconjunto de ativos selecionados.

## Impact

- **Backend**:
  - `BacktestController.BacktestRequest`: inclusão de `List<String> tickers`.
  - `ExecuteBacktestPort` e `ExecuteBacktestUseCase`: filtragem dos ativos recuperados de `AssetRepositoryPort`.
- **Frontend**:
  - `services/api.js`: inclusão de `tickers` na função `executeBacktest`.
  - `components/BacktestTab.jsx`: integração com `getAssets()`, estado `selectedTickers`, UI de Autocomplete MUI com Chips e botões de ação rápida.
