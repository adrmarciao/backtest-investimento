## Why

Na tela de Plano de Compras, as métricas de Drawdown e Retração Fibonacci do benchmark selecionado (`IBOV` / `IDIV`) exigem que o usuário informe manualmente a Máxima do Ano, Topo Fibonacci e Fundo Fibonacci. Na planilha de referência do investidor, esses dados (máxima e mínima de 52 semanas) são obtidos automaticamente via `GOOGLEFINANCE`. Integrar a captura automática dessas métricas diretamente do Google Finance remove a digitação manual e mantém o Termômetro Macro de Mercado sempre calibrado e preciso.

## What Changes

- **Captura Automática de Máxima e Mínima**: Atualizar o cliente do Google Finance (`GoogleFinanceClient`) para extrair não apenas a cotação atual, mas também a máxima de 52 semanas (`Alto — 52 sem`) e mínima de 52 semanas (`Baixo — 52 sem`) dos benchmarks.
- **Fallback para Yahoo Finance**: Atualizar o cliente fallback do Yahoo Finance (`YahooFinanceClient`) para prover a máxima e mínima de 52 semanas (`fiftyTwoWeekHigh` / `fiftyTwoWeekLow`) caso o Google Finance falhe.
- **Cálculo Automático no Backend**: Atualizar a porta do gateway de cotações e o caso de uso `ManagePurchasePlanAssetsUseCase` para alimentar o serviço `PurchasePlanCalculatorService` com a máxima e mínima capturadas automaticamente quando não houver valores manuais configurados.
- **Atualização na Interface**: Atualizar o componente `PurchasePlanTab.jsx` para exibir a máxima e mínima de 52 semanas capturadas automaticamente no card do Termômetro Macro e ajustar a janela de parâmetros com um indicativo de atualização automática e opção de override manual.

## Capabilities

### Modified Capabilities

- `purchase-plan`: Atualiza o requisito de Termômetro Macro de Mercado para capturar automaticamente a máxima e a mínima do ano/52 semanas do benchmark selecionado a partir do Google Finance para os cálculos de Drawdown e retração Fibonacci.

## Impact

- `backend`:
  - `GoogleFinanceClient.java`: Novo suporte ao parsing de `high52` e `low52`.
  - `YahooFinanceClient.java`: Suporte ao fallback de `high52` e `low52`.
  - `MarketQuoteGatewayPort.java` & `MarketQuoteGatewayAdapter.java`: Retorno estendido de cotação com máxima e mínima.
  - `ManagePurchasePlanAssetsUseCase.java`: Integração da cotação estendida com o serviço de cálculo de benchmark.
  - `BenchmarkStatus.java`: Garantia de campos e descrições formatadas.
- `frontend`:
  - `PurchasePlanTab.jsx`: Exibição automática no card Termômetro Macro e indicação de modo automático na caixa de diálogos de parâmetros.
