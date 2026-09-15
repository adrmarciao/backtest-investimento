## Why

Em ativos como BBSE3 (seguradoras) ou bancos, determinados indicadores fundamentalistas (como Dívida/EBITDA) não se aplicam ou são omitidos.
Atualmente, o motor de backtest rejeita o ano se um indicador for nulo. A nova regra deve ignorar travamentos de indicadores nulos em anos cadastrados (aceitando como `true`), mantendo a regra de que anos inteiramente não cadastrados não executam compras.

## What Changes

- **Filtros Fixos Flexíveis**: Em `isYearEligible`, se um indicador (P/L, P/VP, Dív/EBITDA, ROE) for nulo em um ano cadastrado, sua trava é ignorada (aceita como `true`), reprovando apenas quando o indicador estiver presente e violar o limite estabelecido.
- **Tetos de Preço Flexíveis**: Se Teto Bazin ou Teto Graham forem nulos por falta de DPA ou LPA/VPA, a trava do teto ausente é ignorada (aceita como `true`), exigindo apenas que os tetos presentes sejam respeitados.
- **Manutenção do Bloqueio por Ano Não Cadastrado**: Se o ano inteiro não possuir registro no histórico fundamentalista (`annualInd == null`), o motor não executa compras naquele ano.

## Capabilities

### New Capabilities

### Modified Capabilities
- `backtest-engine`: Flexibilização de validação de indicadores nulos em anos cadastrados no motor de backtest.

## Impact

- **Backend**: `BacktestEngine.java` e testes de unidade em `BacktestEngineTest.java`.
- **Frontend**: Nenhuma alteração visual necessária.
