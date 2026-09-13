## 1. Configuração do Projeto e Infraestrutura

- [x] 1.1 Criar estrutura de diretórios do projeto: `backend/` (Spring Boot via Spring Initializr) e `frontend/` (React via Vite), e verificar que ambos inicializam sem erros
- [x] 1.2 Configurar o backend Spring Boot com dependências: Spring Web, Spring WebFlux (para WebClient), Spring Data MongoDB, Spring Boot DevTools, Jackson (JSON), e verificar que `mvn spring-boot:run` sobe na porta 8080
- [x] 1.3 Criar estrutura de pacotes Clean Architecture no backend: `domain/entity/`, `domain/service/`, `domain/port/in/`, `domain/port/out/`, `application/usecase/`, `infrastructure/adapter/in/rest/`, `infrastructure/adapter/out/persistence/`, `infrastructure/adapter/out/gateway/`, `infrastructure/config/`; verificar compilação
- [x] 1.4 Criar o projeto React com Vite (`npm create vite@latest frontend -- --template react`) e instalar dependências: Recharts, Axios; verificar que `npm run dev` serve na porta 5173
- [x] 1.5 Criar `docker-compose.yml` na raiz do projeto com serviços: `mongodb` (mongo:7.0, porta 27017), `backend` (Spring Boot, porta 8080, depende de mongodb), `frontend` (React Vite dev, porta 5173); verificar que `docker-compose up --build` sobe todos os serviços sem erros
- [x] 1.6 Criar Dockerfile para backend (multi-stage: Maven build + JRE runtime) e Dockerfile para frontend (Node com Vite dev server); verificar build das imagens
- [x] 1.7 Configurar `application.yml` do Spring Boot com conexão MongoDB (`spring.data.mongodb.uri=mongodb://mongodb:27017/backtest`) e perfis dev/docker; verificar conexão ao MongoDB no container
- [x] 1.8 Configurar proxy no Vite para redirecionar `/api/*` ao backend e verificar que chamada de teste chega ao backend no docker-compose

## 2. Domínio — Entidades e Serviços (domain/)

- [x] 2.1 Criar entidades de domínio puras (sem annotations Spring/Mongo): `Asset` (ticker, valorAporte, periodicidade enum SEMANAL/MENSAL), `FixedCriteria` (plMax, pvpMax, dividaEbitdaMax, roeMin), `AnnualIndicators` (ticker, ano, pl, pvp, dividaEbitda, roe, dpa, lpa, vpa), `Purchase` (data, ticker, preco, valorAportado, cotas, tetoBazin, tetoGraham), `BacktestResult` (lista de compras, serie temporal, resumo por ativo, anosIgnorados); verificar que nenhuma classe importa pacotes de Spring ou Mongo
- [x] 2.2 Criar `PriceCeilingCalculator` no domínio com métodos estáticos: `calculateBazin(dpa)` retorna `DPA / 0.06` ou null se DPA ≤ 0, `calculateGraham(lpa, vpa)` retorna `√(22.5 × LPA × VPA)` ou null se LPA ≤ 0 ou VPA ≤ 0; verificar com testes unitários
- [x] 2.3 Criar interfaces de ports de saída (domain/port/out/): `AssetRepositoryPort`, `FixedCriteriaRepositoryPort`, `AnnualIndicatorsRepositoryPort`, `BacktestResultRepositoryPort` (CRUD), `PriceFetcherGatewayPort` (busca preços por ticker, intervalo, período); verificar compilação
- [x] 2.4 Criar interfaces de ports de entrada (domain/port/in/): `RegisterAssetPort`, `SaveFixedCriteriaPort`, `SaveAnnualIndicatorsPort`, `ExecuteBacktestPort`, `GetBacktestResultPort`; verificar compilação
- [x] 2.5 Criar `BacktestEngine` no domínio com lógica de 2 fases: Fase 1 — verificar critérios fixos (P/L ≤ max, P/VP ≤ max, Dív/EBITDA ≤ max, ROE ≥ min) contra indicadores anuais; Fase 2 — para cada período de ano elegível, verificar preço ≤ Teto Bazin E preço ≤ Teto Graham e executar compra (aporte ÷ preço = cotas); verificar com testes unitários para cenários: ano elegível com compra, ano elegível sem compra (preço alto), ano não elegível
- [x] 2.6 Criar `MetricsCalculator` no domínio com métodos para: retorno total, CAGR, Maximum Drawdown, Sharpe Ratio, alfa vs IBOVESPA, resumo de compras (total aportes, cotas por ativo, preço médio); verificar com testes unitários

## 3. Application — Use Cases (application/)

- [x] 3.1 Implementar `RegisterAssetUseCase` que usa `AssetRepositoryPort` para salvar/buscar/excluir ativos com validação (ticker não vazio, aporte > 0, periodicidade válida); verificar com testes unitários mockando o port
- [x] 3.2 Implementar `SaveFixedCriteriaUseCase` que usa `FixedCriteriaRepositoryPort` para salvar/carregar critérios fixos com validação de valores positivos; verificar com testes unitários
- [x] 3.3 Implementar `SaveAnnualIndicatorsUseCase` que usa `AnnualIndicatorsRepositoryPort` para salvar/buscar/editar/excluir indicadores por ticker × ano com validação dos 7 campos obrigatórios; verificar com testes unitários
- [x] 3.4 Implementar `ExecuteBacktestUseCase` que orquestra: carregar ativos, critérios e indicadores dos ports; buscar preços via `PriceFetcherGatewayPort`; invocar `BacktestEngine`; calcular métricas via `MetricsCalculator`; salvar resultado via `BacktestResultRepositoryPort`; verificar com teste de integração mockando os ports
- [x] 3.5 Implementar `GetBacktestResultUseCase` que usa `BacktestResultRepositoryPort` para buscar resultados salvos; verificar com teste unitário

## 4. Infrastructure — Persistência MongoDB (infrastructure/adapter/out/persistence/)

- [x] 4.1 Criar MongoDB Documents: `AssetDocument`, `FixedCriteriaDocument`, `AnnualIndicatorsDocument`, `BacktestResultDocument` com annotations `@Document` e `@Id`; verificar mapeamento
- [x] 4.2 Criar mappers bidirecionais entre entidades de domínio e documents MongoDB (ex: `AssetMapper.toDocument(Asset)` e `AssetMapper.toDomain(AssetDocument)`); verificar com testes unitários
- [x] 4.3 Criar Spring Data MongoDB repositories (interfaces `MongoRepository`): `AssetMongoRepository`, `FixedCriteriaMongoRepository`, `AnnualIndicatorsMongoRepository`, `BacktestResultMongoRepository`; verificar operações básicas com Testcontainers ou embedded MongoDB
- [x] 4.4 Criar implementações dos ports de saída que delegam para os MongoRepository: `AssetRepositoryAdapter implements AssetRepositoryPort`, `FixedCriteriaRepositoryAdapter`, `AnnualIndicatorsRepositoryAdapter`, `BacktestResultRepositoryAdapter`; verificar com testes de integração usando Testcontainers
- [x] 4.5 Criar collection `price_cache` com índice TTL de 24h para cache de preços; verificar que documentos expiram após 24h

## 5. Infrastructure — Gateway HTTP Yahoo Finance Java (infrastructure/adapter/out/gateway/)

- [x] 5.1 Criar `YahooFinanceClient` utilizando Spring `WebClient` para realizar requisições à API REST do Yahoo Finance (`query1.finance.yahoo.com/v8/finance/chart/`) com parsing de resposta JSON (timestamps e close prices); verificar busca de histórico de `PETR4.SA`
- [x] 5.2 Implementar suporte ao benchmark IBOVESPA (`^BVSP`) no `YahooFinanceClient`; verificar retorno de cotações históricas
- [x] 5.3 Criar `YahooFinancePriceFetcherGatewayAdapter implements PriceFetcherGatewayPort` que consulta o cache MongoDB (`price_cache`) e, em caso de miss, invoca o `YahooFinanceClient`, salva o resultado no cache com TTL de 24h e lida com retries/resiliência; verificar com teste unitário mockando o WebClient

## 6. Infrastructure — Controllers REST (infrastructure/adapter/in/rest/)

- [x] 6.1 Criar `AssetController` com endpoints: `POST /api/v1/assets`, `GET /api/v1/assets`, `GET /api/v1/assets/{ticker}`, `DELETE /api/v1/assets/{ticker}`; delega para `RegisterAssetPort`; verificar retorno de erro 400 para aporte ≤ 0
- [x] 6.2 Criar `CriteriaController` com endpoints: `PUT /api/v1/criteria`, `GET /api/v1/criteria`; delega para `SaveFixedCriteriaPort`; verificar persistência e recuperação
- [x] 6.3 Criar `IndicatorsController` com endpoints: `POST /api/v1/assets/{ticker}/indicators`, `GET /api/v1/assets/{ticker}/indicators`, `PUT /api/v1/assets/{ticker}/indicators/{year}`, `DELETE /api/v1/assets/{ticker}/indicators/{year}`; delega para `SaveAnnualIndicatorsPort`; verificar que retorno inclui tetos Bazin e Graham calculados
- [x] 6.4 Criar `BacktestController` com endpoints: `POST /api/v1/backtest` (executa backtest), `GET /api/v1/backtest/{id}` (busca resultado salvo), `GET /api/v1/backtest` (lista resultados); delega para `ExecuteBacktestPort` e `GetBacktestResultPort`; verificar com teste de integração
- [x] 6.5 Configurar CORS no Spring Boot para aceitar requisições do frontend (localhost:5173); verificar acesso cross-origin

## 7. Frontend React

- [x] 7.1 Implementar formulário de cadastro de ativo (ticker, valor aporte, periodicidade semanal/mensal) com validações client-side; verificar persistência via API
- [x] 7.2 Implementar formulário de critérios fixos (P/L máx, P/VP máx, Dívida/EBITDA máx, ROE mín); verificar persistência via API
- [x] 7.3 Implementar formulário de cadastro de indicadores anuais por ativo com tabela editável mostrando os 7 indicadores + tetos Bazin e Graham calculados; verificar exibição de N/A quando DPA/LPA/VPA ≤ 0
- [x] 7.4 Implementar listagem de ativos cadastrados com opção de editar/excluir e visualização dos indicadores por ano; verificar navegação entre ativos
- [x] 7.5 Criar componente `PortfolioChart` com Recharts `LineChart` exibindo evolução do portfólio e IBOVESPA normalizados (base 100), com marcadores de compra; verificar renderização com dados de teste
- [x] 7.6 Adicionar tooltip customizado ao `PortfolioChart` mostrando data, valor do portfólio, valor do IBOVESPA e diferença percentual; verificar exibição
- [x] 7.7 Criar componente `MetricsPanel` com cards para retorno total, CAGR, Maximum Drawdown, Sharpe Ratio, alfa vs IBOV, total aportado e valor atual, com indicação verde/vermelho; verificar renderização
- [x] 7.8 Criar componente `PurchasesTable` com tabela paginada (20/página) do histórico de compras: data, ticker, preço, aporte, cotas, Teto Bazin, Teto Graham; verificar paginação e estado vazio
- [x] 7.9 Implementar botão "Exportar CSV" com download de CSV contendo série temporal e lista de compras; verificar download
- [x] 7.10 Implementar estado de carregamento: spinner durante execução do backtest, botão desabilitado, e mensagem de erro em caso de falha; verificar comportamento

## 8. Integração e Validação Final

- [x] 8.1 Teste end-to-end com docker-compose: `docker-compose up --build`, cadastrar WEGE3 com aporte R$500 mensal, definir critérios P/L ≤ 15, P/VP ≤ 3, Dív/EBITDA ≤ 2, ROE ≥ 15%, cadastrar indicadores de 2020-2024, executar backtest e verificar gráfico, métricas e tabela de compras no frontend
- [x] 8.2 Verificar cenário sem compras: cadastrar critérios impossíveis (P/L ≤ 0) e confirmar que o sistema retorna resultado com portfólio vazio e mensagem adequada
- [x] 8.3 Verificar que todos os dados persistem no MongoDB entre reinicializações dos containers (volume mapeado para MongoDB)
- [x] 8.4 Validar responsividade do frontend em telas de 1280px, 1024px e 768px de largura
- [x] 8.5 Verificar que a regra de dependência Clean Architecture é respeitada: nenhuma classe em `domain/` importa pacotes de `org.springframework`, `org.mongodb` ou qualquer framework externo
