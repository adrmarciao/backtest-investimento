## ADDED Requirements

### Requirement: Calcular estatísticas de proventos e reinvestimento
O sistema SHALL calcular o total acumulado de dividendos recebidos no período, o valor total reinvestido em compras de cotas e o saldo final de dividendos não investidos retidos em caixa.

#### Scenario: Métricas de dividendos calculadas
- **WHEN** a simulação é concluída com reinvestimento de dividendos habilitado
- **THEN** o sistema SHALL retornar no resumo das estatísticas: total de dividendos recebidos, total de dividendos reinvestidos e saldo final em caixa de dividendos
