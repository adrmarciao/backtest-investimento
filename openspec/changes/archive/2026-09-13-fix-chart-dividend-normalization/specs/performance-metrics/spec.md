## MODIFIED Requirements

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

### Requirement: Gerar resumo de operações
O sistema SHALL gerar estatísticas sobre as compras realizadas: total de aportes de bolso (excluindo reinvestimentos de dividendos), total investido incluindo reinvestimentos, número de cotas acumuladas por ativo, preço médio de compra por ativo. O preço médio e o retorno por ativo SHALL ser calculados usando apenas o valor aportado de bolso como base.

#### Scenario: Resumo de compras gerado
- **WHEN** o backtest executa ao menos uma compra
- **THEN** o sistema SHALL retornar total de aportes de bolso realizados (excluindo reinvestimentos), valor total investido (incluindo reinvestimentos), cotas por ativo e preço médio por ativo baseado nos aportes de bolso
