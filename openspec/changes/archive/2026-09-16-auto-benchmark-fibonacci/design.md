## Context

Atualmente, o `GoogleFinanceClient` busca apenas a cotação pontual (`precoAtual`) do benchmark. Os campos `altaAno`, `fiboUp` e `fiboDown` são armazenados no documento `PurchasePlanConfig` e exigem calibração manual pelo usuário. Ver proposta em `proposal.md`.

## Goals / Non-Goals

**Goals:**
- Estender a captura de dados do Google Finance para obter a Máxima de 52 semanas (`altaAno` / `fiboUp`) e Mínima de 52 semanas (`fiboDown`) do benchmark selecionado (`IBOV` / `IDIV`).
- Atualizar o fallback do Yahoo Finance para extrair a máxima e mínima de 52 semanas (`fiftyTwoWeekHigh` / `fiftyTwoWeekLow`) quando o Google Finance não responder.
- Atualizar o backend para priorizar os valores capturados automaticamente e usar as configurações manuais do MongoDB apenas se o usuário as tiver preenchido explicitamente como override.
- Atualizar o frontend no componente `PurchasePlanTab.jsx` para exibir os dados de 52 semanas obtidos automaticamente e permitir resetar ou sobrescrever manualmente.

**Non-Goals:**
- Modificar o modelo de armazenamento do MongoDB ou alterar a fórmula de cálculo da Retração Fibonacci e do Drawdown.
- Alterar a busca de dados fundamentalistas dos ativos via Brapi.

## Decisions

### Decisão 1: Modelo de Dados de Cotação Estendida (`MarketQuoteDetails`)
- **Abordagem**: Criar um record ou DTO `MarketQuoteDetails(BigDecimal price, BigDecimal high52Week, BigDecimal low52Week)` para ser retornado pela porta `MarketQuoteGatewayPort`.
- **Raciocínio**: Encapsular o preço atual, a máxima de 52 semanas e a mínima de 52 semanas em um único objeto evita múltiplos métodos na interface e chamadas redundantes.
- **Alternativas consideradas**: Criar métodos separados `fetchHigh52` e `fetchLow52`. Rejeitado por duplicar requisições HTTP para a mesma página do Google Finance.

### Decisão 2: Regex de Parsing do HTML do Google Finance
- **Abordagem**: Utilizar padrões de expressão regular para capturar os elementos `<div class="SwQK7">(Alto — 52 sem|52-week high)</div><div class="dO6ijd">(valor)</div>` e `<div class="SwQK7">(Baixo — 52 sem|52-week low)</div><div class="dO6ijd">(valor)</div>`.
- **Raciocínio**: O HTML do Google Finance utiliza essas classes padrão em português e inglês para apresentar dados do painel lateral.
- **Alternativas consideradas**: Parsing completo com jsoup. Rejeitado por ser mais pesado e o regex direcionado resolver com eficiência e sem dependências adicionais.

### Decisão 3: Resolução de Precedência (Automático vs Manual)
- **Abordagem**: Em `ManagePurchasePlanAssetsUseCase.getBenchmarkStatus(benchmark)`:
  - `effectiveAlta = (config != null && config.getAltaAno() != null) ? config.getAltaAno() : quoteDetails.high52Week()`
  - `effectiveFiboUp = (config != null && config.getFiboUp() != null) ? config.getFiboUp() : quoteDetails.high52Week()`
  - `effectiveFiboDown = (config != null && config.getFiboDown() != null) ? config.getFiboDown() : quoteDetails.low52Week()`
- **Raciocínio**: Dá flexibilidade total. Se o usuário limpar/deixar nulo no modal, o sistema usa 100% automático do Google Finance. Se digitar um valor customizado, respeita o override do usuário.

## Risks / Trade-offs

- **[Risco] Mudança na estrutura do HTML do Google Finance** → **Mitigação**: O fallback transparente no Yahoo Finance (`YahooFinanceClient`) obtém `fiftyTwoWeekHigh` e `fiftyTwoWeekLow` direto da API JSON do Yahoo Finance.
- **[Risco] Formatação de números com ponto e vírgula** → **Mitigação**: Reutilizar a função `parseCleanNumber()` já existente e testada no `GoogleFinanceClient`.
