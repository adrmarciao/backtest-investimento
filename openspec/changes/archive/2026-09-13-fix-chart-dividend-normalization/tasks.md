## 1. Corrigir normalização da série temporal

- [x] 1.1 Em `ExecuteBacktestUseCase.buildTimeSeries()`, condicionar a soma de `accumulatedInvested` para excluir compras com `isReinvestimento=true`. Apenas aportes de bolso devem ser somados ao denominador da normalização base 100. Verificar: compilar o backend com `mvn compile` sem erros.

- [x] 1.2 Adicionar teste unitário para `buildTimeSeries` validando que, dado um cenário com compras regulares e de reinvestimento, o campo `valorInvestidoAcumulado` do `TimeSeriesPoint` contém apenas o valor dos aportes regulares, e `patrimonioNormalizado` reflete o ganho das cotas de reinvestimento. Verificar: `mvn test` passa com o novo teste.

## 2. Corrigir cálculo de total aportado nas métricas

- [x] 2.1 Em `MetricsCalculator.calculateTotalInvested()`, filtrar compras com `isReinvestimento=true` da soma de `valorAportado`, retornando apenas aportes de bolso. Verificar: compilar o backend com `mvn compile` sem erros.

- [x] 2.2 Adicionar teste unitário para `calculateTotalInvested` validando que compras de reinvestimento são excluídas do total. Verificar: `mvn test` passa com o novo teste.

## 3. Corrigir resumo por ativo

- [x] 3.1 Em `MetricsCalculator.calculateAssetSummaries()`, excluir compras com `isReinvestimento=true` do `totalAportadoMap` (mantendo-as no `totalCotasMap`), para que preço médio e retorno reflitam apenas aportes de bolso. Verificar: compilar o backend com `mvn compile` sem erros.

- [x] 3.2 Adicionar teste unitário para `calculateAssetSummaries` validando que o preço médio usa apenas aportes de bolso como numerador, mas inclui todas as cotas (regulares + reinvestimento) no denominador. Verificar: `mvn test` passa com o novo teste.

## 4. Validação integrada

- [x] 4.1 Executar a suíte completa de testes (`mvn test`) garantindo que todos os testes existentes passam e nenhuma regressão foi introduzida. Verificar: build verde com 0 falhas.
