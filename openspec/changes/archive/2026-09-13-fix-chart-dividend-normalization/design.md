## Context

O backtest engine já distingue compras regulares de reinvestimentos de dividendos via `Purchase.isReinvestimento` (campo `Boolean`). O `BacktestEngine.processAssetPurchasesWithDividends()` seta esse flag corretamente. O problema está em dois consumidores downstream que não filtram por esse flag:

1. `ExecuteBacktestUseCase.buildTimeSeries()` — soma `valorAportado` de todas as compras no `accumulatedInvested`
2. `MetricsCalculator` — soma `valorAportado` de todas as compras em `calculateTotalInvested()` e `calculateAssetSummaries()`

Ver proposal.md para motivação.

## Goals / Non-Goals

**Goals:**
- Excluir reinvestimentos de dividendos do denominador na normalização base 100 do gráfico
- Excluir reinvestimentos de dividendos do "total aportado" usado para retorno total e CAGR
- Excluir reinvestimentos do `totalAportadoMap` no resumo por ativo, corrigindo preço médio e retorno
- Manter as cotas de reinvestimento no numerador (patrimônio = todas as cotas × preço)

**Non-Goals:**
- Alterar o `BacktestEngine` — ele já funciona corretamente
- Alterar o frontend — os dados chegam corretos via API
- Migrar dados persistidos — backtests antigos mantêm os valores antigos, só o re-run corrige
- Adicionar campos novos na API (ex: separar "investido de bolso" vs "investido total") — pode ser feito em change futuro

## Decisions

### Decisão 1: Filtrar pelo flag `isReinvestimento` ao invés de rastrear separadamente

**Escolha**: Usar `Purchase.getIsReinvestimento()` para condicionar a soma do `valorAportado`.

**Alternativa considerada**: Manter dois acumuladores separados (`accumulatedFromPocket` e `accumulatedFromDividends`) no `buildTimeSeries`.

**Racional**: O flag já existe e é confiável — adicionado na change `reinvest-dividends`. Usar o flag é um diff mínimo (condição `if` em 3 pontos) e evita refatoração da estrutura de dados.

### Decisão 2: `calculateAssetSummaries` — excluir reinvestimentos do `totalAportadoMap`

**Escolha**: Excluir reinvestimentos do `totalAportadoMap` mas **manter** no `totalCotasMap`.

**Racional**: O preço médio é `totalAporte / totalCotas` — se excluirmos reinvestimentos do aporte mas mantivermos todas as cotas, o preço médio reflete o custo real "de bolso" por cota. As cotas de reinvestimento são efetivamente "bônus" do ponto de vista do investidor. O retorno por ativo compara o valor atual vs o investido de bolso, mostrando o retorno real incluindo o efeito dos dividendos.

### Decisão 3: Não alterar o campo `valorInvestidoAcumulado` no `TimeSeriesPoint`

**Escolha**: O campo `valorInvestidoAcumulado` no `TimeSeriesPoint` passará a conter apenas aportes de bolso (será o denominador da normalização).

**Racional**: Esse campo alimenta o tooltip ("Aportado Acumulado") e a normalização. Manter coerência semântica: se o gráfico normaliza por aportes de bolso, o tooltip deve mostrar o mesmo valor. Caso contrário o tooltip contradiz o gráfico.

## Risks / Trade-offs

**[Dados históricos inconsistentes]** → Backtests persistidos no MongoDB mantêm valores antigos. Mitigação: O usuário pode re-executar o backtest para obter valores corrigidos. Nenhuma migração automática é necessária — os dados antigos não são perigosos, apenas imprecisos.

**[Preço médio por ativo muda de semântica]** → Antes era "custo total / cotas totais", agora será "custo de bolso / cotas totais". Mitigação: A semântica nova é mais útil — mostra o custo real por cota considerando dividendos como retorno. É a convenção padrão em carteiras de investimento.

**[CAGR pode subir significativamente]** → Com denominador menor, o ratio `valorFinal/totalAportado` sobe e o CAGR reflete um retorno mais alto. Mitigação: Isso é o valor correto — o CAGR anterior subestimava o retorno da estratégia por tratar dividendos reinvestidos como "dinheiro novo".
