## Why

O gráfico de evolução patrimonial (base 100) está achatado porque a normalização `patrimônio / investido_acumulado × 100` soma reinvestimentos de dividendos no denominador (`accumulatedInvested`). Quando dividendos são reinvestidos, numerador e denominador sobem juntos, fazendo o gráfico parecer que ignora os reinvestimentos. A mesma lógica infla o "total aportado" nas métricas (retorno total e CAGR), subestimando o retorno real da estratégia. O investidor vê métricas pessimistas e um gráfico que não reflete o benefício dos dividendos reinvestidos.

## What Changes

- Filtrar compras de reinvestimento de dividendos (`isReinvestimento=true`) do acumulado investido na construção da série temporal (`buildTimeSeries`), para que a normalização base 100 reflita apenas dinheiro efetivamente aportado pelo investidor.
- Filtrar compras de reinvestimento de dividendos do cálculo de `totalAportado` no `MetricsCalculator.calculateTotalInvested()`, para que retorno total e CAGR usem o investimento real de bolso como base.
- Filtrar compras de reinvestimento do `totalAportadoMap` no `MetricsCalculator.calculateAssetSummaries()`, corrigindo o preço médio e retorno por ativo para considerar apenas aportes de bolso.
- O tooltip do gráfico que mostra "Aportado Acumulado" passará a exibir apenas aportes de bolso, mantendo coerência com a normalização.

## Capabilities

### New Capabilities

_Nenhuma nova capability._

### Modified Capabilities

- `performance-metrics`: O cálculo de "total aportado" passa a excluir compras de reinvestimento de dividendos. Isso altera a base para retorno total, CAGR e resumo por ativo.
- `results-visualization`: A normalização base 100 do gráfico e o campo "Aportado Acumulado" no tooltip passam a refletir apenas aportes de bolso, não contando reinvestimentos de dividendos no denominador.

## Impact

- **Backend**: `ExecuteBacktestUseCase.buildTimeSeries()` e `MetricsCalculator` (3 métodos).
- **Frontend**: Nenhuma alteração de código necessária — os dados já chegam corretos via API.
- **Dados persistidos**: Backtests já salvos no MongoDB terão valores antigos. Resultados corrigidos aparecem ao re-executar o backtest.
- **APIs**: Sem alteração no contrato da API — mesmos campos, valores corrigidos.
- **Breaking changes**: Nenhum — a interface se mantém idêntica, apenas os valores calculados mudam.
