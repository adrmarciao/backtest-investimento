## MODIFIED Requirements

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

## ADDED Requirements

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
