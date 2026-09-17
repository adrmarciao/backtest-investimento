## Purpose

Interface para edição em massa e salvamento global ("Aplicar Alterações") de indicadores fundamentalistas por ativo.

## Requirements

### Requirement: Edição contínua e em lote na listagem
O sistema SHALL permitir que o usuário altere múltiplos anos e indicadores na tabela de histórico fundamentalista sem a necessidade de persistir individualmente cada linha.

#### Scenario: Edição simultânea de múltiplos anos
- **WHEN** o usuário modifica valores de indicadores em diferentes linhas da tabela
- **THEN** o sistema SHALL armazenar as alterações em memória como rascunho pendente e sinalizar visualmente as linhas modificadas

### Requirement: Aplicação global das alterações ("Aplicar Alterações")
O sistema SHALL disponibilizar um botão global de ação ("Aplicar Alterações") contendo o contador de alterações pendentes, que envia e grava todas as modificações no backend.

#### Scenario: Salvamento bem-sucedido em lote
- **WHEN** o usuário aciona o botão "Aplicar Alterações"
- **THEN** o sistema SHALL persistir todos os anos adicionados ou modificados no backend, atualizar a listagem e limpar os indicadores de pendência

#### Scenario: Descarte de alterações pendentes
- **WHEN** o usuário clica em "Descartar Alterações"
- **THEN** o sistema SHALL restaurar o estado da tabela para os dados originais sem afetar a base de dados

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
