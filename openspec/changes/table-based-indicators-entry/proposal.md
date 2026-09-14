## Why

Ao cadastrar dados fundamentalistas de um ativo, o fluxo atual exige o preenchimento obrigatório de todos os 8 indicadores em um formulário no topo da página antes que o ano possa ser salvo e exibido na listagem. Isso gera fricção quando o usuário deseja criar o ano e ir preenchendo os indicadores de forma fluida e direta na tabela.

## What Changes

- **Simplificação da Adição de Ano**: Inclusão de ação rápida para criar um novo ano apenas selecionando/digitando o ano desejado, inserindo a linha diretamente na tabela e abrindo o modo de edição inline automaticamente.
- **Edição Inline Direta na Listagem**: Otimização do fluxo de edição na tabela para que o usuário possa tabular e preencher os indicadores diretamente nas colunas.
- **Suporte a Preenchimento Parcial**: Permite adicionar o ano e preencher apenas os indicadores desejados no momento, com validação flexível.
- **Preview de Tetos em Tempo Real na Tabela**: Atualização instantânea dos Tetos Bazin e Graham durante a edição das células na tabela antes de salvar.

## Capabilities

### New Capabilities
- `fundamental-indicators`: Gerenciamento e cadastro fluído de indicadores fundamentalistas anuais por ativo.

### Modified Capabilities

## Impact

- **Frontend**: Componentes de gerenciamento de indicadores (`IndicatorsTab.jsx`) atualizados para oferecer criação rápida de ano e edição ágil inline na tabela.
- **Backend**: Compatibilidade mantida com os endpoints REST existentes (`/api/v1/assets/{ticker}/indicators`).
