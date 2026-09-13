## Purpose

Cálculo e exposição das métricas quantitativas de desempenho de um backtest buy-only, permitindo ao usuário avaliar a eficácia de sua estratégia de value investing.

## ADDED Requirements

### Requirement: Calcular retorno total e anualizado
O sistema SHALL calcular o retorno percentual total do portfólio e o retorno anualizado (CAGR). O retorno é calculado comparando o valor de mercado atual do portfólio (cotas acumuladas × preço atual) com o total aportado.

#### Scenario: Retorno total calculado
- **WHEN** o backtest termina com pelo menos uma compra realizada
- **THEN** o sistema SHALL retornar retorno total em percentual ((valor_final - total_aportado) / total_aportado × 100) e o CAGR para o período

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
O sistema SHALL gerar estatísticas sobre as compras realizadas: total de aportes, total investido, número de cotas acumuladas por ativo, preço médio de compra por ativo.

#### Scenario: Resumo de compras gerado
- **WHEN** o backtest executa ao menos uma compra
- **THEN** o sistema SHALL retornar total de aportes realizados, valor total investido, cotas por ativo e preço médio por ativo
