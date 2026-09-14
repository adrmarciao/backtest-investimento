## Purpose

Fornece interface fluida para cadastro, inclusão rápida de anos e edição inline de indicadores fundamentalistas por ativo.

## ADDED Requirements

### Requirement: Adição rápida de ano de indicadores fundamentalistas
O sistema SHALL permitir que o usuário crie uma nova entrada anual de indicadores fornecendo apenas o ano desejado na interface de gerenciamento.

#### Scenario: Adição rápida de novo ano
- **WHEN** o usuário informa o ano e aciona a opção de adicionar novo ano
- **THEN** o sistema SHALL criar o registro do ano selecionado e abrir a respectiva linha na tabela em modo de edição inline

### Requirement: Preenchimento e edição inline na tabela
O sistema SHALL permitir a edição e preenchimento dos indicadores (P/L, P/VP, Dív/EBITDA, ROE, DPA, LPA, VPA) diretamente nas células da listagem do ativo.

#### Scenario: Edição inline e salvamento de indicadores
- **WHEN** o usuário altera os valores dos indicadores na linha da tabela e confirma a gravação
- **THEN** o sistema SHALL persistir a atualização no histórico fundamentalista do ativo

#### Scenario: Suporte a preenchimento parcial
- **WHEN** um indicador específico é omitido ou deixado em branco na edição
- **THEN** o sistema SHALL permitir a gravação do registro mantendo o campo como nulo sem interromper o salvamento

### Requirement: Preview em tempo real dos Tetos de Valuation na tabela
O sistema SHALL calcular e recalcular em tempo real os valores dos Tetos Bazin e Graham exibidos na linha da tabela conforme os campos DPA, LPA e VPA são alterados.

#### Scenario: Atualização dinâmica dos tetos durante a digitação
- **WHEN** o usuário modifica os valores de DPA, LPA ou VPA na célula editável
- **THEN** o sistema SHALL recalcular e reexibir os preços-teto instantaneamente na listagem
