## Purpose

Cadastro de ativos com valor de aporte e periodicidade, definição de critérios fundamentalistas fixos de compra, e cadastro dos indicadores reais por ano para cada ativo.

## Requirements

### Requirement: Cadastrar ativo para backtest
O sistema SHALL permitir ao usuário cadastrar um ativo informando: ticker (ex: WEGE3), valor do aporte em reais e periodicidade dos aportes (semanal ou mensal).

#### Scenario: Ativo cadastrado com sucesso
- **WHEN** o usuário informa ticker válido, valor de aporte positivo e periodicidade (semanal ou mensal)
- **THEN** o sistema SHALL persistir o ativo e exibi-lo na lista de ativos cadastrados

#### Scenario: Ticker duplicado
- **WHEN** o usuário tenta cadastrar um ticker que já existe
- **THEN** o sistema SHALL rejeitar e retornar mensagem indicando que o ativo já está cadastrado

#### Scenario: Valor de aporte inválido
- **WHEN** o usuário informa valor de aporte menor ou igual a zero
- **THEN** o sistema SHALL rejeitar e retornar mensagem de validação

### Requirement: Configurar critérios fundamentalistas fixos de compra
O sistema SHALL permitir ao usuário definir os critérios fixos que valem para todo o período do backtest: P/L máximo, P/VP máximo, Dívida/EBITDA máximo e ROE mínimo.

#### Scenario: Critérios fixos salvos com sucesso
- **WHEN** o usuário preenche P/L máx, P/VP máx, Dívida/EBITDA máx e ROE mín com valores numéricos válidos
- **THEN** o sistema SHALL persistir os critérios e confirmá-los ao usuário

#### Scenario: Valor inválido em critério
- **WHEN** o usuário informa um valor não numérico ou negativo para um critério
- **THEN** o sistema SHALL rejeitar e retornar mensagem indicando o campo inválido

### Requirement: Cadastrar indicadores fundamentalistas reais por ano
O sistema SHALL permitir ao usuário cadastrar e alterar, para cada ativo e cada ano, os seguintes indicadores fundamentalistas reais: P/L, P/VP, Dívida/EBITDA, ROE, DPA (Dividendo Por Ação), LPA (Lucro Por Ação) e VPA (Valor Patrimonial por Ação). A alteração de registros existentes SHALL ser acessível diretamente em cada linha da tabela do histórico fundamentalista na própria tela de listagem.

#### Scenario: Indicadores de um ano cadastrados com sucesso
- **WHEN** o usuário seleciona um ativo e um ano, preenche os 7 indicadores com valores numéricos
- **THEN** o sistema SHALL persistir os indicadores e exibi-los na tabela de indicadores do ativo

#### Scenario: Ano duplicado para o mesmo ativo
- **WHEN** o usuário tenta cadastrar indicadores para um ano que já possui dados cadastrados para aquele ativo
- **THEN** o sistema SHALL permitir editar/sobrescrever os valores existentes

#### Scenario: Indicadores incompletos
- **WHEN** o usuário deixa algum dos 7 indicadores em branco
- **THEN** o sistema SHALL rejeitar e indicar quais campos estão faltando

#### Scenario: Edição inline de indicadores na própria tabela de histórico
- **WHEN** o usuário clica no botão de edição (✏️) em uma linha do ano no Histórico Fundamentalista Cadastrado da tela de listagem
- **THEN** o sistema SHALL transformar as células dessa linha em campos numéricos editáveis na própria tabela sem navegar para outra tela
- **AND** o sistema SHALL recalcular em tempo real os preços-teto de Bazin e Graham conforme os novos valores numéricos são digitados
- **WHEN** o usuário clica no botão de confirmação (✓) da linha
- **THEN** o sistema SHALL enviar a requisição de atualização do ano (`PUT`), persistir as alterações e retornar a linha ao modo de exibição normal

### Requirement: Calcular preços-teto automaticamente
O sistema SHALL calcular automaticamente dois preços-teto para cada ano cadastrado a partir dos indicadores informados:
- **Preço Teto Bazin** = DPA ÷ 0,06
- **Preço Teto Graham** = √(22,5 × LPA × VPA)

#### Scenario: Tetos calculados e exibidos
- **WHEN** o usuário cadastra os indicadores de um ano com DPA, LPA e VPA válidos
- **THEN** o sistema SHALL calcular e exibir o Preço Teto Bazin e o Preço Teto Graham para aquele ano

#### Scenario: DPA zero ou negativo
- **WHEN** o DPA cadastrado é ≤ 0
- **THEN** o sistema SHALL exibir Preço Teto Bazin como "N/A — DPA insuficiente" e considerar o critério Bazin como não satisfeito no backtest

#### Scenario: LPA ou VPA zero ou negativo
- **WHEN** o LPA ou VPA cadastrado é ≤ 0
- **THEN** o sistema SHALL exibir Preço Teto Graham como "N/A — LPA/VPA insuficiente" e considerar o critério Graham como não satisfeito no backtest

### Requirement: Salvar e carregar configurações
O sistema SHALL permitir que o usuário salve uma configuração completa (ativos + critérios + indicadores anuais) com um nome e a recupere posteriormente.

#### Scenario: Configuração salva e recuperada
- **WHEN** o usuário salva a configuração com nome "Bazin Conservador 2020-2024"
- **THEN** o sistema SHALL persistir todos os dados e permitir carregar a configuração pelo nome em sessões futuras
