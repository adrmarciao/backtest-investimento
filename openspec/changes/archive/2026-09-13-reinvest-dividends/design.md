## Context

Ver `proposal.md` para motivação e `specs/` para requisitos de comportamento.
Atualmente o motor de backtest (`backtest-engine`) em Spring Boot / Python itera pelas semanas/meses verificando critérios de ativos e executando ordens de compra baseadas apenas nos aportes fixos.

Para suportar reinvestimento de dividendos:
1. Precisamos buscar a série histórica de dividendos do Yahoo Finance via `yfinance` (`ticker.dividends` ou `actions`).
2. No loop de execução do backtest, ao simular a linha do tempo:
   - Identificar quando ocorrem distribuições de proventos em datas com cotas já acumuladas no portfólio.
   - Creditar `valor_recebido = cotas * dividendo_por_ação` em uma variável de controle `saldoCaixaDividendos`.
   - Na oportunidade de compra (ou data de pagamento), tentar executar uma ordem de compra adicional utilizando o `saldoCaixaDividendos`, condicionada aos critérios de filtro da estratégia (P/L, P/VP, Dívida/EBITDA, ROE, Teto Bazin e Teto Graham).
   - Se os critérios forem satisfeitos: calcular `cotas_compradas = Math.floor(saldoCaixaDividendos / preco_atual)`, abater do caixa e adicionar as cotas ao portfólio.
   - Se os critérios NÃO forem satisfeitos: manter o `saldoCaixaDividendos` retido no caixa e acumulá-lo para a próxima data de simulação.

## Goals / Non-Goals

**Goals:**
- Obter histórico de dividendos por ativo via Yahoo Finance com cache local de 24h.
- Manter o caixa de dividendos acumulados de forma transparente ao longo da linha do tempo.
- Reinvestir dividendos respeitando rigorosamente todas as condições de elegibilidade e preço-teto da estratégia.
- Exibir os dividendos recebidos, total reinvestido e saldo remanescente em caixa nas respostas do backend e na interface React.

**Non-Goals:**
- Não permitir aportes manuais adicionais fora do agendamento configurado (somente reinvestimento de proventos do próprio portfólio).
- Não realizar venda de cotas para rebalanceamento (permanece modelo buy-only).

## Decisions

### Decisão 1: Fluxo de Acúmulo e Reinvestimento no Loop do Backtest
- **Opção Escolhida**: Processar proventos no início de cada passo temporal (semana/mês). Verificar o saldo de caixa de dividendos juntamente com o aporte regular do período.
- **Motivação**: Mantém a simulação determinística e coerente com a granularidade definida (semanal ou mensal), consolidando tanto o aporte novo quanto o saldo acumulado de dividendos para avaliar a compra.
- **Alternativas Consideradas**: Executar compras assíncronas no exato dia útil do pagamento do dividendo. *Descartada* porque a base de preços do backtest é parametrizada em granularidade semanal/mensal.

### Decisão 2: Filtragem de Compras por Regras de Value Investing
- **Opção Escolhida**: Se a semana/mês não atender a todos os critérios (Fase 1 e Fase 2), NENHUMA compra de reinvestimento é feita e todo o caixa de proventos é preservado integralmente para os períodos subsequentes.
- **Motivação**: Atende perfeitamente ao pedido do usuário de não "queimar" dividendos comprando topo ou ações fora dos parâmetros de Bazin/Graham.

### Decisão 3: Armazenamento em Cache dos Dados de Dividendos
- **Opção Escolhida**: Armazenar os dividendos retornados pelo Yahoo Finance na mesma estrutura de cache de preços de mercado (MongoDB ou cache local de 24h).
- **Motivação**: Evita chamadas excessivas ao Yahoo Finance e reduz o tempo de resposta em execuções repetidas.

## Risks / Trade-offs

- **[Risco]** Ausência ou discrepância de dados de dividendos em tickers específicos no Yahoo Finance.
  - *Mitigação*: Tratar retornos vazios ou nulos de forma graciosa sem interromper o backtest; exibir aviso no log de execução se os dados de dividendos não puderem ser recuperados.
- **[Risco]** Saldo de proventos insuficiente para comprar 1 cota inteira.
  - *Mitigação*: Efetuar a compra apenas do número inteiro de cotas `Math.floor(caixa / preço)` e manter o saldo fracionado em caixa de dividendos.
