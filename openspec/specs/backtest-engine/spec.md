## Purpose

Motor de backtest buy-only que processa períodos semanais ou mensais, verifica critérios fundamentalistas fixos contra dados anuais cadastrados, calcula preços-teto Bazin e Graham, e executa compras quando todos os critérios são satisfeitos.

## Requirements

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

### Requirement: Acumular e gerenciar caixa de dividendos recebidos
O sistema SHALL identificar o pagamento de dividendos ao longo do tempo com base no número de cotas do ativo acumuladas até a data ex/pagamento e creditar o valor total (cotas × valor_dividendo_por_ação) no saldo em caixa de dividendos do portfólio.

#### Scenario: Dividendos recebidos e creditados no caixa
- **WHEN** ocorre uma data de pagamento/data-ex de dividendo para um ativo com cotas mantidas no portfólio
- **THEN** o sistema SHALL calcular o valor recebido (`cotas × dividendo_por_ação`) e adicionar ao saldo em caixa de dividendos acumulados

#### Scenario: Ativo sem cotas na data do dividendo
- **WHEN** um dividendo é distribuído, mas o portfólio possui zero cotas daquele ativo na data
- **THEN** o sistema SHALL ignorar a distribuição e não alterar o caixa de dividendos

### Requirement: Executar reinvestimento condicional de dividendos
O sistema SHALL tentar reinvestir o saldo acumulado no caixa de dividendos na próxima oportunidade de compra (ou data de pagamento), aplicando os mesmos critérios de compra da estratégia (critérios fixos P/L, P/VP, Dívida/EBITDA, ROE e preços-teto Bazin e Graham). Se os critérios de compra forem satisfeitos, o sistema compra o máximo de cotas possíveis com o caixa de dividendos. Se os critérios NÃO forem satisfeitos, o saldo de dividendos permanece retido no caixa e é somado para a próxima oportunidade.

#### Scenario: Reinvestimento com critérios satisfeitos
- **WHEN** há saldo positivo no caixa de dividendos e os critérios fundamentalistas e de preço-teto são satisfeitos na data
- **THEN** o sistema SHALL executar a compra de cotas adicionais usando o caixa de dividendos disponível e abater o valor gasto do caixa

#### Scenario: Reinvestimento impedido por critérios não satisfeitos
- **WHEN** há saldo positivo no caixa de dividendos, mas um ou mais critérios da estratégia (P/L, P/VP, Dív/EBITDA, ROE, Bazin ou Graham) falham na data
- **THEN** o sistema SHALL não executar compra de reinvestimento naquela data e acumular o saldo em caixa para a próxima verificação

#### Scenario: Saldo residual no caixa de dividendos
- **WHEN** o valor em caixa de dividendos é menor do que o preço de 1 cota inteira do ativo no momento do reinvestimento
- **THEN** o sistema SHALL comprar apenas o número máximo de cotas inteiras possíveis e manter o troco/saldo residual no caixa de dividendos
