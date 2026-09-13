## MODIFIED Requirements

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
