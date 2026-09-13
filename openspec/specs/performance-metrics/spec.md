## Purpose

Cálculo e exposição das métricas quantitativas de desempenho de um backtest buy-only, permitindo ao usuário avaliar a eficácia de sua estratégia de value investing.

## Requirements

### Requirement: Calcular retorno total e anualizado
O sistema SHALL calcular o retorno percentual total do portfólio e o retorno anualizado (CAGR). O retorno é calculado comparando o valor de mercado atual do portfólio (cotas acumuladas × preço atual) com o total aportado **de bolso** (excluindo valores reinvestidos a partir de dividendos).

#### Scenario: Retorno total calculated
- **WHEN** o backtest termina com pelo menos uma compra realizada
- **THEN** o sistema SHALL retornar retorno total em percentual ((valor_final - total_aportado_de_bolso) / total_aportado_de_bolso × 100) e o CAGR para o período, onde total_aportado_de_bolso exclui compras marcadas como reinvestimento de dividendos

#### Scenario: Período inferior a 1 ano
- **WHEN** o período simulado é inferior a 12 meses
- **THEN** o sistema SHALL calcular apenas o retorno total e indicar CAGR como não aplicável

#### Scenario: Nenhuma compra realizada
- **WHEN** nenhum período satisfez os critérios durante toda a simulação
- **THEN** o sistema SHALL retornar retorno total de 0% e indicar que não houve investimento

### Requirement: Calcular drawdown máximo
O sistema SHALL calcular o maior declínio percentual do valor do portfólio desde um pico até o vale subsequente (Maximum Drawdown).

#### Scenario: Drawdown máximo identificado
- **WHEN** o portfólio tem períodos de queda no valor de mercado
- **THEN** o sistema SHALL retornar o Maximum Drawdown em percentual com datas de pico e vale

#### Scenario: Portfólio sem queda
- **WHEN** o valor do portfólio cresce monotonicamente
- **THEN** o sistema SHALL retornar Maximum Drawdown de 0%

### Requirement: Calcular Sharpe Ratio
O sistema SHALL calcular o Sharpe Ratio usando a taxa Selic média do período como taxa livre de risco.

#### Scenario: Sharpe Ratio calculado
- **WHEN** a simulação retorna série temporal de retornos do portfólio
- **THEN** o sistema SHALL calcular o Sharpe Ratio anualizado

### Requirement: Comparar com benchmark IBOVESPA
O sistema SHALL comparar o retorno da estratégia com o retorno do IBOVESPA para o mesmo período, mostrando alfa (diferença de retorno).

#### Scenario: Comparação com IBOVESPA apresentada
- **WHEN** o backtest termina
- **THEN** o sistema SHALL retornar: retorno da estratégia, retorno do IBOVESPA, alfa e se a estratégia superou o índice

### Requirement: Gerar resumo de operações
O sistema SHALL gerar estatísticas sobre as compras realizadas: total de aportes de bolso (excluindo reinvestimentos de dividendos), total investido incluindo reinvestimentos, número de cotas acumuladas por ativo, preço médio de compra por ativo. O preço médio e o retorno por ativo SHALL ser calculados usando apenas o valor aportado de bolso como base.

#### Scenario: Resumo de compras gerado
- **WHEN** o backtest executa ao menos uma compra
- **THEN** o sistema SHALL retornar total de aportes de bolso realizados (excluindo reinvestimentos), valor total investido (incluindo reinvestimentos), cotas por ativo e preço médio por ativo baseado nos aportes de bolso

### Requirement: Calcular estatísticas de proventos e reinvestimento
O sistema SHALL calcular o total acumulado de dividendos recebidos no período, o valor total reinvestido em compras de cotas e o saldo final de dividendos não investidos retidos em caixa.

#### Scenario: Métricas de dividendos calculadas
- **WHEN** a simulação é concluída com reinvestimento de dividendos habilitado
- **THEN** o sistema SHALL retornar no resumo das estatísticas: total de dividendos recebidos, total de dividendos reinvestidos e saldo final em caixa de dividendos
