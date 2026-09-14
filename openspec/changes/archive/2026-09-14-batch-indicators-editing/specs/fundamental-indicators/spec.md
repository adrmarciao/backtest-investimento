## Purpose

Interface para edição em massa e salvamento global ("Aplicar Alterações") de indicadores fundamentalistas por ativo.

## ADDED Requirements

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
