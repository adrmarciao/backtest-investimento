## Why

Investidores que operam com value investing na B3 precisam validar suas estratégias com dados históricos antes de alocar capital real. O diferencial deste sistema é usar indicadores fundamentalistas reais de cada ano (inseridos manualmente pelo usuário), evitando o look-ahead bias de aplicar dados atuais retroativamente. A estratégia é buy-only com aportes periódicos, usando critérios de Décio Bazin e Benjamin Graham para definição de preço teto.

## What Changes

- Novo sistema de backtest buy-only que permite cadastrar ativos, definir critérios fundamentalistas fixos (P/L, P/VP, Dívida/EBITDA, ROE) e cadastrar os indicadores reais de cada ano
- Cálculo automático de Preço Teto Bazin (DPA ÷ 0,06) e Preço Teto Graham (√(22,5 × LPA × VPA)) por ano a partir dos dados fundamentalistas cadastrados
- Motor de backtest que, para cada período (semanal/mensal), verifica se os fundamentos anuais passam os critérios fixos e se o preço real está abaixo de ambos os tetos calculados — se sim, executa a compra com o valor de aporte configurado
- Backend em Spring Boot com Clean Architecture (domain, application, infrastructure) para orquestrar simulações e buscar preços históricos via Yahoo Finance REST API (Spring WebClient)
- Persistência em MongoDB para ativos, indicadores, critérios e resultados de backtest
- Frontend em React para cadastro de ativos, indicadores, configuração de estratégia, execução de backtest e visualização de resultados
- API REST para comunicação entre frontend e backend
- docker-compose para orquestrar o ambiente (frontend, backend, MongoDB)

## Capabilities

### New Capabilities

- `strategy-config`: Cadastro de ativos (ticker, valor do aporte, periodicidade semanal/mensal), definição de critérios fundamentalistas fixos de compra (P/L máx, P/VP máx, Dívida/EBITDA máx, ROE mín) e cadastro dos indicadores reais por ano (P/L, P/VP, Dívida/EBITDA, ROE, DPA, LPA, VPA)
- `backtest-engine`: Motor de backtest buy-only que processa períodos semanais ou mensais, verifica critérios fixos contra fundamentos anuais, calcula tetos Bazin e Graham, compara com preço real do período e executa compra quando todos os critérios são satisfeitos
- `market-data`: Integração via HTTP Client Java com Yahoo Finance REST API para buscar preços históricos de fechamento (semanal/mensal) de ações da B3 e do IBOVESPA para comparação
- `performance-metrics`: Cálculo de métricas: retorno total, retorno anualizado (CAGR), drawdown máximo, Sharpe Ratio e comparação com IBOVESPA (alfa)
- `results-visualization`: Interface React com gráficos de evolução patrimonial, linha do tempo de compras, painel de métricas e comparação com IBOVESPA

### Modified Capabilities

## Impact

- **Novo projeto**: Sistema criado do zero, sem impacto em sistemas existentes
- **Arquitetura**: Clean Architecture no backend (domínio isolado de frameworks)
- **Backend**: Spring Boot (Java) com Clean Architecture e Spring WebClient para buscar preços históricos via Yahoo Finance REST API
- **Banco de dados**: MongoDB (via Spring Data MongoDB)
- **Frontend**: React com biblioteca de gráficos (Recharts)
- **Infraestrutura**: docker-compose orquestrando frontend, backend e MongoDB
- **Dados fundamentalistas**: Entrada manual pelo usuário (não buscados automaticamente) — isso garante dados reais e evita look-ahead bias
- **Dependências externas**: Yahoo Finance REST API (via HTTP) apenas para preços históricos e dados do IBOVESPA
- **APIs novas**: REST API para cadastro de ativos, indicadores anuais, critérios de estratégia, execução de backtest e consulta de resultados/métricas
