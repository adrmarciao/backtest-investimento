## Context

Ver `proposal.md` para motivação e justificativa. O sistema já implementa cache local para preços através da entidade `PriceCacheDocument` e do repositório `PriceCacheMongoRepository`. Esta mudança introduz uma infraestrutura análoga para histórico de dividendos.

## Goals / Non-Goals

**Goals:**
- Implementar cache local de histórico de dividendos no MongoDB com expiração TTL de 24 horas (86.400s).
- Integrar transparentemente o cache ao adapter de gateway `YahooFinancePriceFetcherGatewayAdapter`.
- Garantir que buscas repetidas de dividendos no mesmo período de 24 horas utilizem os dados salvos em cache em vez de realizar chamadas de rede.

**Non-Goals:**
- Alterar a lógica do motor de cálculo de backtest (`BacktestEngine`) ou de métricas (`MetricsCalculator`).
- Alterar as rotas da API REST ou contratos de DTOs expostos ao frontend.

## Decisions

### 1. Criar Coleção Dedicada `dividend_cache` no MongoDB
- **Decisão**: Criar a classe `DividendCacheDocument` mapeando a coleção `dividend_cache` com chave sintética `TICKER_START_END` (ex: `PETR4.SA_2020-01-01_2023-12-31`).
- **Alternativa Considerada**: Estender `PriceCacheDocument` para reutilizar a mesma coleção.
- **Justificativa**: Preços e dividendos possuem granularidades e frequências de atualização distintas (preços possuem periodicidade semanal/mensal, enquanto dividendos são eventos com data-ex). Separar em coleções próprias mantém o esquema limpo e reduz o tamanho dos documentos armazenados.

### 2. TTL de 24 horas via Índice do MongoDB
- **Decisão**: Utilizar a anotação Spring Data `@Indexed(expireAfterSeconds = 86400)` no atributo `createdAt` de `DividendCacheDocument`.
- **Alternativa Considerada**: Controle de expiração manual com consultas por data.
- **Justificativa**: O MongoDB gerencia a remoção automática dos documentos expirados em segundo plano, alinhado à implementação existente de `PriceCacheDocument`.

### 3. Integração no `YahooFinancePriceFetcherGatewayAdapter`
- **Decisão**: Injetar `DividendCacheMongoRepository` no construtor de `YahooFinancePriceFetcherGatewayAdapter`.
- **Fluxo de Leitura/Escrita**:
  1. Construir chave `cacheKey = ticker + "_" + start + "_" + end`.
  2. Consultar `cacheRepository.findById(cacheKey)`.
  3. Se presente (mesmo se a lista for vazia), retornar a lista armazenada.
  4. Se ausente (*miss*), buscar no `YahooFinanceClient.fetchDividends(ticker, start, end)`.
  5. Salvar o documento com `Instant.now()` no MongoDB e retornar o resultado.

## Risks / Trade-offs

- **[Risco] Mutação retroativa em proventos recente pelo Yahoo Finance** → *Mitigação*: Com TTL de 24h, correções ou novos lançamentos de proventos são sincronizados automaticamente após expiração do cache.
- **[Risco] Armazenamento de listas vazias para ativos sem proventos** → *Mitigação*: Armazenar listas vazias evita chamadas externas repetidas (*cache stampede*) para ativos que sabidamente não pagaram dividendos no período.
