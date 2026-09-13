## Why

Atualmente, a recuperação do histórico de dividendos via `YahooFinancePriceFetcherGatewayAdapter` realiza requisições HTTP externas para o Yahoo Finance a cada execução de backtest sem nenhum mecanismo de cache local. Em contraste, o histórico de preços já possui cache em MongoDB com expiração TTL de 24h. Essa assimetria causa latência desnecessária nas simulações, requisições repetidas e riscos de rate limit na API externa do Yahoo Finance.

## What Changes

- **Criar `DividendCacheDocument`**: Documento MongoDB mapeando a coleção `dividend_cache` contendo o id da consulta (`ticker_start_end`), o ticker, a lista de `DividendPayment` e o campo `createdAt` com índice `@Indexed(expireAfterSeconds = 86400)` (TTL 24h).
- **Criar `DividendCacheMongoRepository`**: Interface Spring Data Mongo para a coleção `dividend_cache`.
- **Atualizar `YahooFinancePriceFetcherGatewayAdapter`**: Injetar o novo repositório de cache e implementar a verificação no cache local antes de realizar a requisição externa via `YahooFinanceClient` ao buscar dividendos, salvando os resultados no MongoDB quando houver cache miss.

## Capabilities

### New Capabilities
- None

### Modified Capabilities
- `market-data`: Especifica o comportamento detalhado do cache local de dividendos com retenção TTL de 24h para evitar chamadas externas redundantes.

## Impact

- **Backend**:
  - `com.backtest.backend.infrastructure.adapter.out.persistence.DividendCacheDocument` (Novo)
  - `com.backtest.backend.infrastructure.adapter.out.persistence.DividendCacheMongoRepository` (Novo)
  - `com.backtest.backend.infrastructure.adapter.out.gateway.YahooFinancePriceFetcherGatewayAdapter` (Modificado)
- **Desempenho**: Redução drástica na latência de execução de backtests subsequentes para os mesmos ativos no período de 24 horas.
- **Banco de Dados**: Criação automática do índice TTL na nova coleção `dividend_cache` no MongoDB.
