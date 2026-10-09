## MODIFIED Requirements

### Requirement: Termômetro Macro de Mercado (Fibonacci & Drawdown)
O sistema SHALL exibir o status de retração do mercado com seletor de benchmark entre IBOV e IDIV, obtendo automaticamente a cotação atual, a máxima de 52 semanas (topo Fibonacci) e a mínima de 52 semanas (fundo Fibonacci) diretamente do Google Finance para os cálculos de Drawdown e retração Fibonacci.

#### Scenario: Seleção de Benchmark e exibição de métricas automáticas
- **WHEN** o usuário seleciona IBOV ou IDIV
- **THEN** o sistema SHALL consultar automaticamente no Google Finance a cotação atual, a máxima de 52 semanas e a mínima de 52 semanas do benchmark
- **AND** SHALL calcular e exibir o Drawdown percentual em relação à máxima de 52 semanas e o nível percentual de retração Fibonacci em relação ao intervalo de 52 semanas

#### Scenario: Sobrescrita manual opcional de parâmetros macro
- **WHEN** o usuário informa manualmente os valores de Máxima do Ano, Fibo Up ou Fibo Down na janela de calibração de parâmetros
- **THEN** o sistema SHALL utilizar os valores manuais informados para o cálculo de Drawdown e Fibonacci em substituição aos valores automáticos capturados do Google Finance
