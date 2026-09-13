## Purpose

Integração com Yahoo Finance para busca e cache de preços históricos de fechamento de ações da B3, usados na fase de verificação de preço do motor de backtest. Indicadores fundamentalistas NÃO são buscados por esta integração (são cadastrados manualmente pelo usuário).

## Requirements

### Requirement: Buscar preços históricos de fechamento da B3
O sistema SHALL recuperar preços de fechamento ajustados para ações da B3 via Yahoo Finance (yfinance), usando o sufixo `.SA` nos tickers (ex: `WEGE3.SA`). Os dados devem ser retornados na granularidade solicitada (semanal ou mensal).

#### Scenario: Busca semanal bem-sucedida
- **WHEN** uma requisição é feita para um ticker válido da B3 com intervalo semanal
- **THEN** o sistema SHALL retornar série temporal com preço de fechamento ajustado para cada semana do período solicitado

#### Scenario: Busca mensal bem-sucedida
- **WHEN** uma requisição é feita para um ticker válido da B3 com intervalo mensal
- **THEN** o sistema SHALL retornar série temporal com preço de fechamento ajustado para cada mês do período solicitado

#### Scenario: Ticker inválido ou não disponível
- **WHEN** o ticker solicitado não existe no Yahoo Finance ou não está disponível para o período
- **THEN** o sistema SHALL retornar erro com mensagem indicando ativo não encontrado

#### Scenario: Falha de conexão com Yahoo Finance
- **WHEN** o serviço Yahoo Finance está indisponível ou retorna erro
- **THEN** o sistema SHALL tentar novamente até 3 vezes com backoff exponencial antes de retornar erro

### Requirement: Buscar dados do benchmark IBOVESPA
O sistema SHALL recuperar dados históricos de preço do índice IBOVESPA (`^BVSP`) com o mesmo período e granularidade dos dados de ativos simulados, para uso na comparação de desempenho.

#### Scenario: Dados do IBOVESPA recuperados para o período
- **WHEN** uma simulação é executada para um período definido
- **THEN** o sistema SHALL buscar automaticamente os preços de fechamento do IBOVESPA para o mesmo período e granularidade

### Requirement: Cachear preços localmente
O sistema SHALL armazenar em cache os preços históricos buscados do Yahoo Finance para evitar requisições repetidas. O cache SHALL expirar após 24 horas.

#### Scenario: Preços servidos do cache
- **WHEN** uma requisição é feita para um ticker e período já buscados recentemente (< 24h)
- **THEN** o sistema SHALL retornar os dados do cache sem realizar nova chamada ao Yahoo Finance

### Requirement: Buscar histórico de dividendos e proventos da B3
O sistema SHALL recuperar o histórico de proventos e dividendos distribuídos (valor por ação e data-ex/pagamento) para ativos da B3 via Yahoo Finance (yfinance), mantendo um cache local de 24h no MongoDB para evitar chamadas de rede redundantes.

#### Scenario: Dividendos recuperados com sucesso
- **WHEN** o motor de backtest solicita o histórico de dividendos de um ativo válido da B3 para o período simulado
- **THEN** o sistema SHALL retornar a série temporal de proventos pagos por ação com suas respectivas datas e armazenar o resultado no cache local MongoDB se houver cache miss

#### Scenario: Ativo sem histórico de dividendos no período
- **WHEN** o ativo não distribuiu proventos no período simulado
- **THEN** o sistema SHALL retornar uma lista vazia de dividendos sem gerar erro

#### Scenario: Cache de dividendos local
- **WHEN** o histórico de dividendos de um ativo foi buscado há menos de 24 horas
- **THEN** o sistema SHALL retornar os dados de dividendos a partir do cache local no MongoDB sem realizar nova requisição externa ao Yahoo Finance
