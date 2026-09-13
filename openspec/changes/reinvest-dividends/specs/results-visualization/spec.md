## ADDED Requirements

### Requirement: Exibir métricas e histórico de dividendos reinvestidos
O sistema SHALL apresentar no painel de resultados cards indicativos do valor total de dividendos recebidos, total reinvestido e saldo em caixa, além de identificar na tabela de compras quais operações foram realizadas através de reinvestimento de proventos.

#### Scenario: Visualização de dividendos na interface
- **WHEN** os resultados do backtest são renderizados no frontend
- **THEN** o sistema SHALL exibir os cards de métricas de dividendos e destacar as compras de reinvestimento na tabela de histórico
