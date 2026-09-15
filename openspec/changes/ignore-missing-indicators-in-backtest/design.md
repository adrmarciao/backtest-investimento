## Context

Veja `proposal.md` para a motivação.
Ajustaremos a lógica do motor de backtest (`BacktestEngine.java`) para tratar valores nulos de indicadores em anos cadastrados como travamentos ignorados (aceitos como `true`), mantendo o bloqueio de compras quando o ano inteiro não estiver no cadastro fundamentalista (`annualInd == null`).

## Goals / Non-Goals

**Goals:**
- Ajustar `isYearEligible` para ignorar filtros cujos indicadores sejam nulos no ano cadastrado.
- Ajustar `processAssetPurchasesWithDividends` para aceitar a compra quando um dos tetos (Bazin ou Graham) for nulo devido a dados ausentes.
- Preservar a trava de ano não cadastrado (`annualInd == null`), que impede qualquer compra naquele período.

**Non-Goals:**
- Alterar o `PriceCeilingCalculator` (continua retornando `null` para entradas nulas/inválidas).

## Decisions

### Decisão 1: Checagem com Null-Guard nos Filtros Fixos
- **Escolha**: Cada filtro em `isYearEligible` só executa a validação se tanto a meta do critério quanto o valor do indicador forem não nulos:
```java
if (criteria.getPlMax() != null && indicators.getPl() != null) {
    if (indicators.getPl().compareTo(criteria.getPlMax()) > 0) {
        return false;
    }
}
```

### Decisão 2: Validação Flexível de Tetos de Preço
- **Escolha**:
```java
boolean bazinOk = (tetoBazin == null) || (currentPrice.compareTo(tetoBazin) <= 0);
boolean grahamOk = (tetoGraham == null) || (currentPrice.compareTo(tetoGraham) <= 0);

if (bazinOk && grahamOk) {
    // Executa a compra
}
```

## Risks / Trade-offs

- **[Risco]** Quebra de testes de unidade legados que esperavam que nulo rejeitasse o ano.
  → *Mitigação*: Atualizar e expandir a suíte em `BacktestEngineTest.java` cobrindo cenários com indicadores nulos em anos cadastrados e anos não cadastrados.
