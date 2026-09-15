## MODIFIED Requirements

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
