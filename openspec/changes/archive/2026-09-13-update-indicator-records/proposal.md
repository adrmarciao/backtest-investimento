## Why

Ao cadastrar indicadores fundamentalistas anuais para um ativo, o usuário pode digitar um valor incorreto (como ROE, P/L, DPA, etc.). Atualmente a única forma de corrigir é excluindo o ano e reescrevendo todos os 7 indicadores do zero. Adicionar a funcionalidade de edição inline diretamente na tabela do histórico fundamentalista facilitará correções rápidas de registros existentes na própria tela de listagem, sem trocas de páginas ou navegações adicionais.

## What Changes

- **Frontend (`IndicatorsTab.jsx`)**:
  - Adicionar o botão de edição (✏️ Editar) em cada linha da tabela na seção "Histórico Fundamentalista Cadastrado".
  - Permitir a edição em linha (inline editing) dos 7 indicadores (`pl`, `pvp`, `dividaEbitda`, `roe`, `dpa`, `lpa`, `vpa`) para o ano selecionado.
  - Recalcular dinamicamente em tempo real os preços-teto de **Bazin** e **Graham** na própria linha durante a edição.
  - Adicionar o botão de confirmação (✓ Salvar) para disparar a atualização (`PUT /api/v1/assets/{ticker}/indicators/{year}`) e o botão de cancelamento (✕ Cancelar) para descartar alterações.

## Capabilities

### Modified Capabilities
- `strategy-config`: Atualizar o requisito de cadastro de indicadores anuais para permitir a alteração e atualização inline de registros cadastrados para um ticker na própria listagem.

## Impact

- **Frontend**: Componente `IndicatorsTab.jsx` gerenciando estado da linha em edição (`editingYear`, `editFormData`).
- **Backend / API**: O backend e o serviço da API (`updateIndicators`) já suportam a operação `PUT /api/v1/assets/{ticker}/indicators/{year}`, portanto não há quebra de contrato ou alteração de backend necessária.
