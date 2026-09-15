## Context

Ver `proposal.md` para motivação e escopo da funcionalidade. A tela atual de Backtest dispara a simulação para 100% dos ativos cadastrados no banco sem possibilidade de escolha pelo usuário.

## Goals / Non-Goals

**Goals:**
- Permitir que o usuário selecione/desselecione granularmente os ativos a serem simulados.
- Oferecer uma UX moderna e ágil (MUI `Autocomplete` com Chips e botões "Selecionar Todos" e "Limpar Seleção").
- Adaptar o endpoint `POST /api/v1/backtest` e a camada de domínio no backend para filtrar a execução.

**Non-Goals:**
- Agrupar ativos em carteiras/presets salvos no banco de dados (recurso futuro).
- Alterar o motor de cálculo de indicadores ou a lógica das Fases 1 e 2 do `BacktestEngine`.

## Decisions

### 1. Autocomplete Multi-select com Chips + Botões de Ação no Frontend
- **Decisão**: Usar `<Autocomplete multiple options={availableAssets} getOptionLabel={(option) => option.ticker} .../>` da biblioteca Material UI no componente `BacktestTab.jsx`.
- **Alternativas consideradas**:
  - *Grid/Lista de Checkboxes*: Ocupa muito espaço vertical se o sistema tiver dezenas de ativos.
  - *Select tradicional HTML*: Pobre visualmente e ruim para remover/adicionar ativos individualmente.
- **Justificativa**: O Autocomplete com Chips permite busca rápida digitando o ticker, remoção individual por chip clicável `(x)` e excelente integração visual com o Material UI do projeto.

### 2. Filtragem de Ativos na camada de Use Case no Backend (`ExecuteBacktestUseCase`)
- **Decisão**: No `ExecuteBacktestUseCase.executeBacktest(start, end, tickers)`, carregar a lista de ativos cadastrados via `assetRepositoryPort.findAll()` e filtrar via Java Stream aqueles cujo ticker está presente em `tickers`.
- **Alternativas consideradas**:
  - *Criar método `assetRepositoryPort.findByTickerIn(List<String> tickers)`*: Exigiria alterar os contratos dos adaptadores MongoDB e implementar um método específico de persistência.
- **Justificativa**: Como a lista de ativos cadastrados no sistema de backtest é tipicamente de dezenas a centenas de itens, o filtro em memória no UseCase é extremamente rápido, simples, limpo e não polui os contratos de repositório.

### 3. Tratamento de Retrocompatibilidade no Backend
- **Decisão**: Se `tickers` na DTO `BacktestRequest` for `null` ou vazio `[]`, o backend executa o backtest para **todos** os ativos cadastrados.
- **Justificativa**: Mantém retrocompatibilidade com eventuais chamadas diretas da API sem quebrar testes existentes.

## Risks / Trade-offs

- **[Risco]** Usuário desmarca todos os tickers e clica em enviar → **Mitigação**: O botão "Iniciar Motor de Backtest" ficará desabilitado na UI quando `selectedTickers.length === 0`, exibindo uma mensagem de alerta.
- **[Risco]** Ativos cadastrados sem ticker válido ou com caixa diferente (ex: "petr4" vs "PETR4") → **Mitigação**: O backend converterá a comparação para uppercase / case-insensitive (`ticker.equalsIgnoreCase(...)`).
