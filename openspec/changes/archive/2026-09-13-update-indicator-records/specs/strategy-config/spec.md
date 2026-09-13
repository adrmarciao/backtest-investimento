## MODIFIED Requirements

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
