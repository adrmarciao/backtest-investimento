## Context

Veja `proposal.md` para a motivação.
Atualmente, `IndicatorsTab.jsx` permite salvar edições apenas de um ano por vez.
O objetivo é transformar a interface em um grid de edição em lote (batch edit), permitindo editar livremente múltiplos anos e adicionar novos registros, consolidando o salvamento em um botão único "Aplicar Alterações".

## Goals / Non-Goals

**Goals:**
- Permitir edição de múltiplos anos e inclusão de novos anos diretamente na listagem sem travar a interface em uma única linha.
- Exibir a quantidade de alterações pendentes no painel de ação global (`💾 Aplicar Alterações (N)` e `❌ Descartar Alterações`).
- Exibir indicadores visuais de status por linha na tabela (ex: 🟢 Novo, 🟡 Modificado).
- Persistir todas as alterações acumuladas via chamadas assíncronas em lote (`Promise.all`) ao clicar em "Aplicar Alterações".

**Non-Goals:**
- Alterar APIs REST ou modelo do MongoDB.

## Decisions

### Decisão 1: Gerenciamento de Estado Rascunho (`isDirty` / `isDraft`)
- **Escolha**: Cada item de `indicatorsList` mantém um estado de rascunho com flags `isDirty` (indica se foi modificado) e `isDraft` (indica se foi criado recentemente).
- **Funcionamento**: Ao modificar qualquer célula, a linha é marcada como `isDirty: true`. O botão "Aplicar Alterações" processa apenas os itens com `isDirty` ou `isDraft`.

### Decisão 2: Feedback Visual Transparente
- **Escolha**: Adicionar uma coluna de status visual na tabela indicando `Novo` (verde), `Modificado` (amarelo) ou `Salvo` (sem destaque), permitindo ao usuário revisar as mudanças antes de confirmar.

### Decisão 3: Salvamento em Lote com Promise.all
- **Escolha**: O handler `handleApplyChanges` utiliza `Promise.all` para enviar as requisições de salvamento/atualização dos itens pendentes, recarregando a lista atualizada do backend em seguida.

## Risks / Trade-offs

- **[Risco]** Falha de rede parcial durante a gravação de múltiplos registros.
  → *Mitigação*: Exibir alerta com mensagem amigável e manter no estado rascunho apenas os registros que eventualmente falharem para permitir nova tentativa.
