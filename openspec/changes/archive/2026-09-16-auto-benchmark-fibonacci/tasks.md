## 1. Backend Gateway & Quote Fetcher

- [x] 1.1 Criar record/DTO `MarketQuoteDetails` (preço atual, alta 52 semanas, baixa 52 semanas) e atualizar a interface `MarketQuoteGatewayPort`.
- [x] 1.2 Atualizar `GoogleFinanceClient` para extrair os campos de máxima e mínima de 52 semanas (`Alto — 52 sem` e `Baixo — 52 sem`) do HTML e validar com testes unitários.
- [x] 1.3 Atualizar `YahooFinanceClient` para extrair `fiftyTwoWeekHigh` e `fiftyTwoWeekLow` como fallback de 52 semanas e validar com testes unitários.
- [x] 1.4 Atualizar `MarketQuoteGatewayAdapter` para implementar a chamada de cotação estendida de acordo com a porta.

## 2. Backend Use Case & Domain Logic

- [x] 2.1 Atualizar `ManagePurchasePlanAssetsUseCase.getBenchmarkStatus` para receber os dados automáticos de 52 semanas e resolver a precedência (manual vs automático).
- [x] 2.2 Atualizar testes unitários em `ManagePurchasePlanAssetsUseCaseTest` e `PurchasePlanCalculatorServiceTest` para verificar a atribuição automática e a retração de Fibonacci.

## 3. Frontend & UI Integration

- [x] 3.1 Atualizar `PurchasePlanTab.jsx` para exibir a máxima e a mínima no card do Termômetro Macro diretamente a partir do status do benchmark retornado da API.
- [x] 3.2 Atualizar a janela de diálogo de calibração de parâmetros macro para suportar reset automático e feedback visual ao usuário.
- [x] 3.3 Executar testes do frontend/backend e verificar integração dos cálculos.
