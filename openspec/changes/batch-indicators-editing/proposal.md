## Why

Atualmente, a edição dos indicadores fundamentalistas exige salvar cada ano individualmente. Permitir a edição em massa de múltiplos anos simultaneamente e a aplicação global das alterações ("Aplicar Alterações") torna o preenchimento e a revisão de balanços históricos muito mais rápidos, intuitivos e fluidos.

## What Changes

- **Edição em Massa na Tabela (Grid Edit)**: Permitir alterar valores de múltiplos anos e adicionar novas linhas de anos livremente na tabela.
- **Ação Global "Aplicar Alterações"**: Incluir controle global com o número de alterações pendentes, permitindo persistir todas as modificações de uma só vez ou descartá-las.
- **Indicadores de Estado Rascunho**: Feedback visual claro por linha destacando quais anos foram editados ou adicionados antes da confirmação final.

## Capabilities

### New Capabilities
- `fundamental-indicators`: Suporte a edição em massa (batch) e salvamento com confirmação global de alterações.

### Modified Capabilities

## Impact

- **Frontend**: `IndicatorsTab.jsx` atualizado com gerenciamento de estado rascunho (`draftList`), cálculo de alterações pendentes e salvamento unificado ao clicar em "Aplicar Alterações".
- **Backend**: Integração transparente com a API de persistência existente (`/api/v1/assets/{ticker}/indicators`).
