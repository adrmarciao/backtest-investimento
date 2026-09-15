## 1. Ajuste do Motor de Backtest

- [x] 1.1 Atualizar `isYearEligible` em `BacktestEngine.java` para ignorar travas de indicadores que forem nulos em um ano cadastrado.
- [x] 1.2 Atualizar a validação de preços-teto em `processAssetPurchasesWithDividends` para aceitar compras quando um teto for nulo, mantendo o bloqueio se o ano inteiro não estiver cadastrado (`annualInd == null`).

## 2. Testes de Unidade e Validação

- [x] 2.1 Atualizar os testes em `BacktestEngineTest.java` para verificar o comportamento com indicadores parcialmente nulos em anos cadastrados e confirmar que anos sem cadastro continuam não comprando.
- [x] 2.2 Executar os testes automatizados com `mvn test` no diretório `backend` para garantir 100% de aprovação.
