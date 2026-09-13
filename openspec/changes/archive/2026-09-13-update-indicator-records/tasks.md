## 1. Implementar Estado de Edição Inline no Componente React

- [x] 1.1 Adicionar os estados `editingYear` e `editForm` no componente `IndicatorsTab.jsx` para controlar qual linha da tabela está sendo editada e armazenar os valores dos 7 indicadores.
- [x] 1.2 Atualizar a renderização das linhas na tabela "Histórico Fundamentalista Cadastrado" em `IndicatorsTab.jsx` para alternar entre o modo de exibição de texto e o modo de inputs numéricos editáveis quando `ind.ano === editingYear`.
- [x] 1.3 Adicionar o recálculo em tempo real do Teto Bazin e Teto Graham na própria linha em edição conforme os campos DPA, LPA e VPA são alterados.

## 2. Ações de Salvamento e Cancelamento de Edição

- [x] 2.1 Adicionar os botões de ação na linha em edição: botão de confirmação Check (✓) para invocar `updateIndicators(selectedTicker, editingYear, editForm)`, recarregar a listagem e encerrar edição, e botão Cancelar (✕) para descartar alterações.
- [x] 2.2 Verificar a edição inline no frontend visualmente e testar o salvamento de um indicador alterado para confirmar a atualização na listagem.
