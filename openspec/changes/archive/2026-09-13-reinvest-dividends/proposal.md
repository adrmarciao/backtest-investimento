## Why

Atualmente, o motor de backtest executa apenas compras baseadas no aporte periódico fixo em dinheiro (buy-only), sem considerar os dividendos (e juros sobre capital próprio) distribuídos pelos ativos ao longo do tempo. Permitir o reinvestimento dos dividendos recebidos tornará a simulação muito mais realista e precisa para estratégias de *Value Investing* e *Dividend Investing*, refletindo os efeitos dos juros compostos.

## What Changes

- **Motor de Backtest (`backtest-engine`)**:
  - Acumular proventos/dividendos distribuídos pelos ativos com base na data de pagamento/data-ex durante a linha do tempo do backtest.
  - Manter um saldo em caixa de dividendos acumulados.
  - Tentar reinvestir o saldo em caixa de dividendos na data de execução de compras (ou data-ex/pagamento do dividendo), aplicando integralmente as regras e critérios de compra (P/L, P/VP, Dív/EBITDA, ROE, Teto Bazin e Teto Graham).
  - Caso os critérios de compra não sejam satisfeitos no momento do pagamento/reinvestimento, o saldo de dividendos não é perdido: é acumulado e somado ao valor disponível na próxima oportunidade de compra.
- **Dados de Mercado (`market-data`)**:
  - Obter o histórico de proventos/dividendos por ação via Yahoo Finance (`yfinance`) para cada ticker simulado no período.
- **Métricas e Visualização (`performance-metrics` e `results-visualization`)**:
  - Exibir total de dividendos recebidos, total reinvestido e saldo remanescente nos resultados do backtest e na interface do usuário.

## Capabilities

### New Capabilities
- None

### Modified Capabilities
- `market-data`: Adicionar recuperação e armazenamento em cache do histórico de proventos/dividendos dos ativos via Yahoo Finance.
- `backtest-engine`: Incorporar acúmulo de dividendos recebidos, controle de saldo em caixa de proventos e execução de reinvestimento condicionado aos critérios fundamentalistas da estratégia.
- `performance-metrics`: Incluir métricas de proventos recebidos, total reinvestido e saldo em caixa nos resultados do backtest.
- `results-visualization`: Exibir os eventos de reinvestimento e métricas de dividendos nos gráficos e tabelas da interface.

## Impact

- **Backend (Python / Java / Services)**:
  - Integração `yfinance`: requisição de `ticker.dividends` / `ticker.actions`.
  - Simulação de Backtest: atualização do loop de simulação temporal para controlar o caixa de proventos e ordens de compra derivadas de dividendos.
- **Frontend (React / Vite)**:
  - Exibição de cards e estatísticas relativas a dividendos reinvestidos na guia de resultados do backtest.
