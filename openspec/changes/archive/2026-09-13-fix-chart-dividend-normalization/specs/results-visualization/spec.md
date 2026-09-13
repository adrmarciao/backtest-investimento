## MODIFIED Requirements

### Requirement: Exibir gráfico de evolução patrimonial
O sistema SHALL exibir um gráfico de linha interativo mostrando a evolução do valor do portfólio ao longo do tempo, com linha paralela do IBOVESPA para comparação, ambos indexados ao valor inicial (base 100). A normalização base 100 do portfólio SHALL usar como denominador apenas o valor aportado de bolso pelo investidor, excluindo compras realizadas com reinvestimento de dividendos.

#### Scenario: Gráfico renderizado após simulação
- **WHEN** o backtest é concluído com sucesso
- **THEN** o sistema SHALL renderizar o gráfico com a evolução do portfólio normalizada como (valor_patrimônio / aportes_de_bolso × 100) e do IBOVESPA normalizado para base 100

#### Scenario: Hover no gráfico mostra detalhes
- **WHEN** o usuário posiciona o cursor sobre um ponto do gráfico
- **THEN** o sistema SHALL exibir tooltip com data, valor do portfólio, valor aportado acumulado (apenas aportes de bolso), valor do IBOVESPA e diferença percentual

#### Scenario: Reinvestimento de dividendos refletido no gráfico
- **WHEN** dividendos são reinvestidos em novas cotas durante o backtest
- **THEN** o valor do portfólio SHALL refletir as cotas adicionais compradas com dividendos, e a normalização base 100 SHALL subir proporcionalmente ao ganho real (denominador mantém apenas aportes de bolso)
