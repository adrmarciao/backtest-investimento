## Purpose

Motor de backtest buy-only que processa períodos semanais ou mensais, verifica critérios fundamentalistas fixos contra dados anuais cadastrados, calcula preços-teto Bazin e Graham, e executa compras quando todos os critérios são satisfeitos.

## Requirements

### Requirement: Executar simulação de backtest buy-only
O sistema SHALL executar uma simulação de backtest para os ativos selecionados (ou todos os ativos cadastrados se nenhuma restrição for especificada), processando cada período (semanal ou mensal) dentro do intervalo de anos com indicadores cadastrados. A simulação é buy-only (sem venda).

#### Scenario: Simulação bem-sucedida com ativos específicos selecionados
- **WHEN** o usuário executa o backtest informando um subconjunto de tickers cadastrados
- **THEN** o sistema SHALL processar as compras, reinvestimentos de dividendos, métricas de desempenho e série temporal considerando exclusivamente os ativos da lista selecionada

#### Scenario: Simulação bem-sucedida com compras realizadas
- **WHEN** o usuário executa o backtest com ativos, critérios e indicadores anuais cadastrados
- **THEN** o sistema SHALL retornar os resultados com histórico de compras, evolução do portfólio e métricas de desempenho

#### Scenario: Simulação sem compras (nenhum critério satisfeito)
- **WHEN** nenhum período de nenhum ativo satisfaz todos os critérios durante todo o intervalo
- **THEN** o sistema SHALL retornar resultado com portfólio vazio (zero cotas) e métricas zeradas

#### Scenario: Ativo sem indicadores para algum ano
- **WHEN** há anos sem indicadores cadastrados dentro do intervalo de simulação
- **THEN** o sistema SHALL pular esses anos e reportar no resultado quais anos foram ignorados por falta de dados

### Requirement: Seleção de tickers para execução do backtest
A interface do motor de backtest SHALL permitir a seleção individual e em lote dos tickers cadastrados que farão parte da simulação.

#### Scenario: Pré-seleção automática de todos os ativos cadastrados
- **WHEN** a tela do motor de backtest é carregada
- **THEN** o sistema SHALL obter os ativos cadastrados e pré-selecionar todos os tickers por padrão no componente de seleção

#### Scenario: Seleção rápida (Selecionar Todos / Limpar Seleção)
- **WHEN** o usuário clica em "Selecionar Todos" ou "Limpar Seleção"
- **THEN** o sistema SHALL marcar ou desmarcar instantaneamente todos os tickers disponíveis na lista de seleção

#### Scenario: Bloqueio de execução sem tickers selecionados
- **WHEN** o usuário desmarca todos os tickers da lista
- **THEN** o sistema SHALL desabilitar o botão de execução do motor de backtest e apresentar mensagem orientando a seleção de pelo menos 1 ativo


### Requirement: Verificar critérios fixos contra fundamentos anuais (Fase 1)
O sistema SHALL, para cada ano com indicadores cadastrados, verificar os critérios fixos configurados (P/L ≤ P/L máx, P/VP ≤ P/VP máx, Dívida/EBITDA ≤ Dívida/EBITDA máx, ROE ≥ ROE mín). Se um indicador específico for nulo (ausente) em um ano cadastrado, o sistema SHALL ignorar a trava daquele indicador específico (considerando-a satisfeita/`true`) e reprovar o ano apenas se o indicador estiver preenchido e violar o limite estabelecido.

#### Scenario: Ano elegível (todos os critérios satisfeitos ou nulos)
- **WHEN** os indicadores reais do ano satisfazem os critérios fixos configurados ou quando os indicadores configurados são nulos
- **THEN** o sistema SHALL marcar o ano como elegível e prosseguir para a verificação de preço (Fase 2)

#### Scenario: Ano não elegível (indicador presente falha)
- **WHEN** um indicador cadastrado não é nulo e não satisfaz o limite fixo correspondente
- **THEN** o sistema SHALL pular os períodos daquele ano (nenhuma compra é feita)

### Requirement: Verificar preço contra tetos Bazin e Graham (Fase 2)
O sistema SHALL, para cada período (semanal/mensal) de um ano com indicadores cadastrados, comparar o preço de fechamento com os preços-teto calculáveis. Se um Preço Teto (Bazin ou Graham) for nulo/ausente devido à omissão de dados (DPA, LPA ou VPA), o sistema SHALL ignorar a trava daquele teto específico. A compra é executada se o preço for menor ou igual aos tetos presentes. Se o ano inteiro não estiver cadastrado no histórico fundamentalista, o sistema SHALL não realizar compras naquele ano.

#### Scenario: Preço dentro dos tetos presentes — compra executada
- **WHEN** o preço de fechamento do período é menor ou igual aos preços-teto calculáveis para o ano
- **THEN** o sistema SHALL registrar a compra usando o valor do aporte

#### Scenario: Preço acima de teto presente — compra não executada
- **WHEN** o preço de fechamento do período excede algum teto presente calculável para o ano
- **THEN** o sistema SHALL não executar compra naquele período

#### Scenario: Teto Bazin ou Graham é nulo por ausência de dado — trava ignorada
- **WHEN** o Teto Bazin ou Teto Graham é nulo devido à ausência de indicador (DPA, LPA ou VPA) em um ano cadastrado
- **THEN** o sistema SHALL ignorar a exigência daquele teto e avaliar a compra apenas pelos tetos restantes

#### Scenario: Ano sem nenhum cadastro fundamentalista — compra não executada
- **WHEN** um ano de negociação não possui nenhum registro cadastrado no histórico fundamentalista do ativo
- **THEN** o sistema SHALL pular o ano e não executar compras no período

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
