## Context

Novo projeto criado do zero. Ver `proposal.md - Why` para motivação. O sistema integra dois componentes principais: frontend React e backend Spring Boot em Java. Os indicadores fundamentalistas são inseridos manualmente pelo usuário (dados reais de cada ano), e o Yahoo Finance é acessado diretamente via requisições HTTP (Spring WebClient) no backend apenas para obter preços históricos de fechamento. A estratégia é buy-only (sem venda) com aportes periódicos.

O backend segue **Clean Architecture** para manter a lógica de domínio isolada de frameworks e infraestrutura. A persistência usa **MongoDB** e o ambiente de desenvolvimento é orquestrado via **docker-compose**.

## Goals / Non-Goals

**Goals:**
- Backend em Clean Architecture: domínio puro (sem dependência de framework), use cases como orquestradores, adapters para REST e persistência
- Persistência em MongoDB (Spring Data MongoDB)
- docker-compose para orquestrar os serviços (frontend, backend, MongoDB)
- Cadastro manual de indicadores fundamentalistas por ativo × ano (P/L, P/VP, Dívida/EBITDA, ROE, DPA, LPA, VPA)
- Critérios fixos de compra definidos uma vez (P/L máx, P/VP máx, Dívida/EBITDA máx, ROE mín)
- Cálculo automático de Preço Teto Bazin (DPA ÷ 0,06) e Preço Teto Graham (√(22,5 × LPA × VPA)) por ano
- Motor de backtest buy-only: aportes periódicos (semanal/mensal) quando todos os critérios são satisfeitos
- Preços históricos reais buscados via Yahoo Finance REST API (Spring WebClient) com cache no MongoDB
- Comparação de resultados com IBOVESPA
- API REST clara entre frontend e backend

**Non-Goals:**
- Venda de ativos (a estratégia é buy-only, acumula posições)
- Trading ao vivo ou integração com corretoras
- Dados em tempo real (apenas histórico)
- Backtesting de opções, FIIs ou outros instrumentos além de ações da B3
- Autenticação/multi-usuário na versão inicial (single user)
- Busca automática de indicadores fundamentalistas (são manuais)
- Serviço ou scripts em Python (toda a integração de dados de mercado é feita em Java)

## Decisions

### 1. Clean Architecture no backend

**Decisão:** O backend segue Clean Architecture com 3 camadas:

```
domain/
├── entity/           ← Entidades de negócio puras (sem annotations Spring/Mongo)
│   Asset, FixedCriteria, AnnualIndicators, BacktestResult, Purchase
├── service/          ← Serviços de domínio (lógica de negócio pura)
│   BacktestEngine, MetricsCalculator, PriceCeilingCalculator
└── port/
    ├── in/           ← Interfaces dos use cases (contratos de entrada)
    │   RegisterAssetPort, SaveFixedCriteriaPort, SaveAnnualIndicatorsPort,
    │   ExecuteBacktestPort, GetBacktestResultPort
    └── out/          ← Interfaces de repositórios e gateways (contratos de saída)
        AssetRepositoryPort, FixedCriteriaRepositoryPort,
        AnnualIndicatorsRepositoryPort, BacktestResultRepositoryPort,
        PriceFetcherGatewayPort

application/
└── usecase/          ← Orquestradores que implementam os ports de entrada
    RegisterAssetUseCase, SaveFixedCriteriaUseCase,
    SaveAnnualIndicatorsUseCase, ExecuteBacktestUseCase

infrastructure/
├── adapter/
│   ├── in/rest/      ← Controllers REST (chama use cases via ports de entrada)
│   └── out/
│       ├── persistence/  ← Implementações MongoDB dos repository ports
│       │   Documents Mongo, MongoRepository interfaces, Mapper entity↔document
│       └── gateway/      ← YahooFinancePriceFetcherGateway (Spring WebClient HTTP)
└── config/           ← Beans Spring, MongoDB config, WebClient config, CORS, etc.
```

**Regra de dependência:** `domain/` não importa nada de `application/` ou `infrastructure/`. `application/` não importa nada de `infrastructure/`. Apenas `infrastructure/` conhece Spring, MongoDB, WebClient.

**Alternativas consideradas:**
- **Arquitetura em camadas tradicional (MVC):** Mais simples, mas acopla lógica de negócio ao framework. Dificulta testes unitários do domínio e troca de infraestrutura.
- **Hexagonal (Ports & Adapters):** Essencialmente o mesmo que Clean Arch — o que estamos fazendo é uma implementação concreta deste conceito.

**Rationale:** O domínio de backtest tem lógica rica (motor de 2 fases, cálculos de teto, métricas). Isolar essa lógica em classes puras facilita testes unitários sem mocks de infraestrutura e permite trocar MongoDB ou fonte de cotação sem tocar no domínio.

### 2. MongoDB como banco de dados

**Decisão:** Toda a persistência usa MongoDB via Spring Data MongoDB.

**Collections:**
- `assets` — Ativos cadastrados (ticker, valorAporte, periodicidade)
- `fixed_criteria` — Critérios fixos de compra (singleton document)
- `annual_indicators` — Indicadores por ativo × ano
- `backtest_results` — Resultados de backtests executados
- `price_cache` — Cache de cotações históricas com TTL

**Alternativas consideradas:**
- **Arquivos JSON locais:** Simples mas frágil, sem queries, sem índices, sem transações.
- **H2/PostgreSQL:** Relacional é viável mas os dados (indicadores anuais, resultados de backtest) são naturalmente documentos JSON aninhados — MongoDB se encaixa melhor.

**Rationale:** Os dados do backtest (resultado com lista de compras, série temporal, métricas) mapeiam naturalmente para documentos. MongoDB facilita armazenar e consultar esses dados sem ORM complexo. Spring Data MongoDB oferece repositórios declarativos com mínimo boilerplate.

### 3. docker-compose para ambiente de desenvolvimento

**Decisão:** Um `docker-compose.yml` na raiz do projeto orquestra os serviços:

```yaml
services:
  mongodb:        # MongoDB 7.0, porta 27017
  backend:        # Spring Boot, porta 8080, depende de mongodb
  frontend:       # React Vite dev server, porta 5173
```

**Alternativas consideradas:**
- **Instalar tudo localmente:** Exige configurar Java, Node, MongoDB individualmente.
- **Incluir serviço Python:** Desnecessário, pois o acesso ao Yahoo Finance é feito diretamente via HTTP no Java.

**Rationale:** docker-compose garante que qualquer desenvolvedor (ou CI) pode rodar `docker-compose up` e ter o ambiente completo funcionando com apenas 3 serviços simples.

### 4. Indicadores fundamentalistas inseridos manualmente

**Decisão:** O usuário cadastra os indicadores fundamentalistas de cada ativo para cada ano (P/L, P/VP, Dívida/EBITDA, ROE, DPA, LPA, VPA). O sistema não busca esses dados automaticamente.

**Alternativas consideradas:**
- **Yahoo Finance**: Não oferece séries históricas confiáveis de fundamentos por ano para ações da B3.
- **APIs brasileiras (StatusInvest, Fundamentus via scraping)**: Instáveis, sem garantia de disponibilidade.

**Rationale:** Dados manuais garantem que o backtest usa os fundamentos reais de cada ano, eliminando look-ahead bias.

### 5. Dois preços-teto calculados: Bazin e Graham

**Decisão:** O sistema calcula automaticamente dois preços-teto por ano:
- **Preço Teto Bazin** = DPA ÷ 0,06
- **Preço Teto Graham** = √(22,5 × LPA × VPA)

Ambos devem ser satisfeitos (preço real ≤ teto Bazin **E** preço real ≤ teto Graham).

**Rationale:** Combinar Bazin (dividendos) e Graham (valor intrínseco) cria um filtro conservador.

### 6. Estratégia buy-only com aportes periódicos

**Decisão:** O sistema executa apenas compras, nunca vendas. O usuário configura aporte e periodicidade (semanal/mensal) por ativo.

**Rationale:** Value investing de longo prazo com foco em acumulação.

### 7. Dois níveis de verificação no motor

**Decisão:** O motor opera em dois níveis:
1. **Nível anual (Fase 1):** Critérios fixos verificados contra fundamentos do ano. Falha → pula o ano.
2. **Nível periódico (Fase 2):** Preço de fechamento verificado contra Teto Bazin E Teto Graham. Satisfeitos → compra.

**Rationale:** Separa avaliação qualitativa (fundamentos) da avaliação de preço (por período).

### 8. Busca de preços via WebClient Java direto no Yahoo Finance API

**Decisão:** O gateway de cotações (`YahooFinancePriceFetcherGateway`) utiliza `Spring WebClient` para realizar requisições HTTP diretas à API REST do Yahoo Finance (`query1.finance.yahoo.com/v8/finance/chart/`).

**Rationale:** Elimina dependência de Python, reduz a complexidade do docker-compose e elimina o overhead de `ProcessBuilder`.

### 9. Frontend React com Recharts para gráficos

**Decisão:** Interface em React (Vite) com Recharts para séries temporais.

**Rationale:** Componentes declarativos e reutilizáveis.

### 10. Cache de preços via MongoDB com TTL de 24h

**Decisão:** Os preços buscados da API do Yahoo Finance são cacheados em uma collection MongoDB `price_cache` com índice TTL de 24h.

**Rationale:** Evita requisições repetidas ao Yahoo Finance e reduz risco de rate limit.

## Risks / Trade-offs

- **[Risco] Yahoo Finance muda a API REST ou aplica rate limit** → Mitigação: o gateway adapter usa cache de 24h no MongoDB e é a única classe que conhece a URL/formato do Yahoo Finance, facilitando fallback se necessário.
- **[Trade-off] Clean Architecture adiciona camadas** → Mais arquivos e indireção, mas o domínio é rico o suficiente para justificar o investimento.
- **[Trade-off] Indicadores manuais dão trabalho** → Preço por dados honestos. Futuramente pode-se adicionar importação via CSV.
- **[Risco] DPA ou LPA negativo gera teto inválido** → Mitigação: o domínio trata DPA ≤ 0 ou LPA/VPA ≤ 0 como critério não satisfeito.
- **[Risco] MongoDB adiciona dependência de infraestrutura** → Mitigação: docker-compose abstrai a instalação. Para testes unitários do domínio, nenhuma dependência de banco é necessária (Clean Arch).

## Migration Plan

Projeto novo, sem migração. Passos de deploy:
1. `docker-compose up --build` (sobe MongoDB, backend, frontend)
2. Acessar frontend em `http://localhost:5173`
3. Backend em `http://localhost:8080`

Para desenvolvimento sem Docker:
1. Instalar MongoDB localmente ou via container avulso
2. `mvn spring-boot:run`
3. `npm run dev`

## Open Questions

- **Taxa Selic histórica para Sharpe Ratio:** Usar API pública do BCB ou valor fixo configurável como fallback?
