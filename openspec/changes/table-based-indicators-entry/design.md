## Context

Veja `proposal.md` para a motivação.
Atualmente, `IndicatorsTab.jsx` possui um formulário fixo com 8 campos obrigatórios acima da tabela e um modo de edição inline dentro da tabela existente.

## Goals / Non-Goals

**Goals:**
- Substituir o formulário superior de 8 campos por uma barra compacta de "Adicionar Novo Ano".
- Ao incluir um ano novo, criar e posicionar a linha correspondente diretamente na tabela já em modo de edição inline (`editingYear = novoAno`).
- Exibir os Tetos Bazin e Graham recalculados dinamicamente na própria linha da tabela enquanto o usuário digita DPA, LPA e VPA.
- Permitir salvamento parcial onde campos não informados são convertidos para `null`.

**Non-Goals:**
- Alterar APIs backend ou esquema de dados no MongoDB (os contratos REST atuais `/api/v1/assets/{ticker}/indicators` já oferecem suporte completo).
- Modificar outras abas do sistema (Filtros ou Backtest Engine).

## Decisions

### Decisão 1: Formulário Compacto + Ativação Inline Instantânea
- **Escolha**: Um card simples de inclusão de ano ("Ano") com botão "+ Adicionar Ano à Tabela".
- **Comportamento**: Ao acionar o botão, valida se o ano já foi cadastrado para o ativo. Se for inédito, adiciona uma linha rascunho na lista e ativa `editingYear = ano` com `editForm` limpo.
- **Alternativas Consideradas**: Manter o formulário gigante e a edição em tabela simultaneamente (descartado por poluído e redundante).

### Decisão 2: Preview em Tempo Real na Tabela
- **Escolha**: Na linha em edição da tabela, as colunas de "Teto Bazin" e "Teto Graham" renderizam os cálculos dinâmicos usando `calculateBazinPreview(editForm.dpa)` e `calculateGrahamPreview(editForm.lpa, editForm.vpa)` reativamente a cada keystroke.

### Decisão 3: Flexibilidade no Salvamento
- **Escolha**: Campos vazios no formulário de edição são convertidos em `null` no objeto enviado para `saveIndicators`, garantindo salvamento sem falhas de conversão de `NaN`.

## Risks / Trade-offs

- **[Risco]** Criação de linha rascunho que o usuário desiste de salvar.
  → *Mitigação*: Ação de cancelar (botão `CloseIcon`) descarta o estado rascunho sem alterar o backend.
