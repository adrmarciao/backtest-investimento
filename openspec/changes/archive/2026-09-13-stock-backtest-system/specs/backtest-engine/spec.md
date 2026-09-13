## Purpose

Motor de backtest buy-only que processa períodos semanais ou mensais, verifica critérios fundamentalistas fixos contra dados anuais cadastrados, calcula preços-teto Bazin e Graham, e executa compras quando todos os critérios são satisfeitos.

## ADDED Requirements

### Requirement: Executar simulação de backtest buy-only
O sistema SHALL executar uma simulação de backtest para cada ativo cadastrado, processando cada período (semanal ou mensal) dentro do intervalo de anos com indicadores cadastrados. A simulação é buy-only (sem venda).

#### Scenario: Simulação bem-sucedida com compras realizadas
- **WHEN** o usuário executa o backtest com ativos, critérios e indicadores anuais cadastrados
- **THEN** o sistema SHALL retornar os resultados com histórico de compras, evolução do portfólio e métricas de desempenho

#### Scenario: Simulação sem compras (nenhum critério satisfeito)
- **WHEN** nenhum período de nenhum ativo satisfaz todos os critérios durante todo o intervalo
- **THEN** o sistema SHALL retornar resultado com portfólio vazio (zero cotas) e métricas zeradas

#### Scenario: Ativo sem indicadores para algum ano
- **WHEN** há anos sem indicadores cadastrados dentro do intervalo de simulação
- **THEN** o sistema SHALL pular esses anos e reportar no resultado quais anos foram ignorados por falta de dados

### Requirement: Verificar critérios fixos contra fundamentos anuais (Fase 1)
O sistema SHALL, para cada ano com indicadores cadastrados, verificar se os 4 critérios fixos são satisfeitos: P/L real ≤ P/L máx, P/VP real ≤ P/VP máx, Dívida/EBITDA real ≤ Dívida/EBITDA máx, ROE real ≥ ROE mín.

#### Scenario: Ano elegível (todos os critérios satisfeitos)
- **WHEN** os indicadores reais do ano satisfazem todos os 4 critérios fixos
- **THEN** o sistema SHALL marcar o ano como elegível e prosseguir para a verificação de preço (Fase 2) em cada período

#### Scenario: Ano não elegível (algum critério falha)
- **WHEN** pelo menos um dos critérios fixos não é satisfeito
- **THEN** o sistema SHALL pular todos os períodos daquele ano (nenhuma compra é feita)

### Requirement: Verificar preço contra tetos Bazin e Graham (Fase 2)
O sistema SHALL, para cada período (semanal/mensal) de um ano elegível, buscar o preço de fechamento real via yfinance e comparar com os preços-teto calculados. A compra é executada apenas quando o preço ≤ Teto Bazin **E** preço ≤ Teto Graham.

#### Scenario: Preço dentro dos dois tetos — compra executada
- **WHEN** o preço de fechamento do período é ≤ Teto Bazin e ≤ Teto Graham
- **THEN** o sistema SHALL registrar compra: valor do aporte ÷ preço = número de cotas adquiridas

#### Scenario: Preço acima de pelo menos um teto — compra não executada
- **WHEN** o preço de fechamento do período excede o Teto Bazin ou o Teto Graham
- **THEN** o sistema SHALL não executar compra naquele período

#### Scenario: Teto Bazin ou Graham é N/A (DPA/LPA/VPA inválido)
- **WHEN** o Teto Bazin ou Teto Graham é N/A para o ano (DPA ≤ 0, LPA ≤ 0 ou VPA ≤ 0)
- **THEN** o sistema SHALL considerar o critério como não satisfeito e não comprar

### Requirement: Registrar histórico de compras
O sistema SHALL manter um log completo de todas as compras realizadas, incluindo: data, ticker, preço de compra, valor aportado, número de cotas adquiridas, Preço Teto Bazin do ano, Preço Teto Graham do ano.

#### Scenario: Compra registrada com detalhes completos
- **WHEN** uma compra é executada
- **THEN** o sistema SHALL persistir o registro com data, ticker, preço, aporte, cotas, teto Bazin e teto Graham

### Requirement: Calcular evolução do portfólio
O sistema SHALL calcular a evolução do valor total do portfólio ao longo do tempo, somando o valor de mercado de todas as cotas acumuladas (cotas × preço de fechamento do período) para cada ponto no tempo.

#### Scenario: Evolução calculada com múltiplos ativos
- **WHEN** o backtest inclui mais de um ativo com compras realizadas
- **THEN** o sistema SHALL calcular o valor total do portfólio como a soma dos valores de cada ativo (cotas_ativo × preço_ativo) para cada período
