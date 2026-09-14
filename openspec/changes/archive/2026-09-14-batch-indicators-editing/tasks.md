## 1. Interface de Edição em Lote e Estado Rascunho

- [x] 1.1 Atualizar `IndicatorsTab.jsx` para suportar estado de rascunho continuo (`draftList`), permitindo edição de qualquer célula em qualquer linha simultaneamente.
- [x] 1.2 Implementar barra de ações globais com indicador de modificações pendentes ("💾 Aplicar Alterações (N)" e "❌ Descartar Alterações").
- [x] 1.3 Adicionar coluna/chip visual de status na tabela destacando anos "Novos" (🟢) e "Modificados" (🟡).

## 2. Persistência em Lote e Descarte

- [x] 2.1 Implementar handler `handleApplyChanges` para persistir todos os registros modificados/adicionados no backend via requisições assíncronas em lote (`Promise.all`).
- [x] 2.2 Implementar handler `handleDiscardChanges` para redefinir o estado da tabela para os dados originais do backend.

## 3. Validação e Testes

- [x] 3.1 Validar edição de múltiplos anos e adição de novo ano simultaneamente e verificar o salvamento completo ao clicar em "Aplicar Alterações".
- [x] 3.2 Executar build do frontend (`npm run build`) para garantir ausência de erros.
