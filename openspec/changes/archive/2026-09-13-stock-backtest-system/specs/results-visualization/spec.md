## Purpose

Interface React que apresenta visualmente os resultados de um backtest buy-only, com gráficos interativos de evolução patrimonial, linha do tempo de compras, painel de métricas e comparação com IBOVESPA.

## ADDED Requirements

### Requirement: Exibir gráfico de evolução patrimonial
O sistema SHALL exibir um gráfico de linha interativo mostrando a evolução do valor do portfólio ao longo do tempo, com linha paralela do IBOVESPA para comparação, ambos indexados ao valor inicial (base 100).

#### Scenario: Gráfico renderizado após simulação
- **WHEN** o backtest é concluído com sucesso
- **THEN** o sistema SHALL renderizar o gráfico com a evolução do portfólio e do IBOVESPA normalizados para base 100

#### Scenario: Hover no gráfico mostra detalhes
- **WHEN** o usuário posiciona o cursor sobre um ponto do gráfico
- **THEN** o sistema SHALL exibir tooltip com data, valor do portfólio, valor do IBOVESPA e diferença percentual

### Requirement: Exibir linha do tempo de compras
O sistema SHALL exibir uma visualização das compras realizadas ao longo do tempo, mostrando em quais períodos o sistema comprou e em quais não comprou, e os motivos.

#### Scenario: Compras marcadas no gráfico
- **WHEN** o backtest inclui compras realizadas
- **THEN** o sistema SHALL marcar os pontos de compra no gráfico de evolução patrimonial com indicador visual (ex: marcador no eixo)

### Requirement: Exibir painel de métricas resumidas
O sistema SHALL exibir um painel com as métricas de desempenho: retorno total, CAGR, Maximum Drawdown, Sharpe Ratio, alfa vs IBOVESPA, total aportado, valor atual do portfólio.

#### Scenario: Métricas exibidas após simulação
- **WHEN** o backtest é concluído
- **THEN** o sistema SHALL exibir as métricas em cards com indicação visual de positivo (verde) ou negativo (vermelho)

### Requirement: Exibir tabela de compras realizadas
O sistema SHALL exibir uma tabela paginada com o histórico de todas as compras: data, ticker, preço de compra, valor aportado, cotas adquiridas, Teto Bazin e Teto Graham do ano.

#### Scenario: Tabela de compras exibida com paginação
- **WHEN** o backtest resulta em mais de 20 compras
- **THEN** o sistema SHALL exibir as compras paginadas com 20 registros por página

#### Scenario: Tabela vazia quando sem compras
- **WHEN** nenhuma compra foi executada
- **THEN** o sistema SHALL exibir mensagem "Nenhuma compra realizada no período — nenhum ativo atendeu a todos os critérios"

### Requirement: Permitir exportação dos resultados
O sistema SHALL permitir ao usuário exportar os resultados do backtest em formato CSV.

#### Scenario: Exportação CSV
- **WHEN** o usuário clica em "Exportar CSV"
- **THEN** o sistema SHALL iniciar download de arquivo CSV com a série temporal do portfólio e lista de compras

### Requirement: Exibir estado de carregamento durante simulação
O sistema SHALL informar ao usuário que a simulação está em execução, impedindo submissão duplicada.

#### Scenario: Indicador de progresso exibido
- **WHEN** o usuário submete o backtest e o backend está processando
- **THEN** o sistema SHALL exibir indicador de carregamento e desabilitar o botão

#### Scenario: Erro de simulação exibido
- **WHEN** o backend retorna erro
- **THEN** o sistema SHALL exibir mensagem de erro descritiva e reabilitar o formulário
