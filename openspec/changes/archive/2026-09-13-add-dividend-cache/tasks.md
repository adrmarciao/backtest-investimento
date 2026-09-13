## 1. Persistência e Entidade de Cache

- [x] 1.1 Criar a classe `DividendCacheDocument` em `infrastructure/adapter/out/persistence` mapeando a coleção `dividend_cache` com TTL de 24h (`@Indexed(expireAfterSeconds = 86400)`) e verificar compilação.
- [x] 1.2 Criar a interface `DividendCacheMongoRepository` estendendo `MongoRepository<DividendCacheDocument, String>` e verificar compilação.

## 2. Adaptação do Gateway e Leitura/Escrita em Cache

- [x] 2.1 Atualizar `YahooFinancePriceFetcherGatewayAdapter` para injetar `DividendCacheMongoRepository` e implementar cache read-through/write-through no método `fetchHistoricalDividends`.
- [x] 2.2 Adicionar testes unitários/integração para verificar que requisições repetidas de dividendos retornam dados do cache sem acionar o cliente HTTP do Yahoo Finance.

## 3. Validação do Sistema

- [x] 3.1 Executar a suíte completa de testes do backend com `./mvnw test` e validar que todos os testes passam sem regressões.
