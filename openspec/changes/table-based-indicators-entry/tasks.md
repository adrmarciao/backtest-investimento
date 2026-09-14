## 1. Interface de Adição Rápida de Ano

- [x] 1.1 Refatorar a área superior de `IndicatorsTab.jsx`, substituindo o formulário de 8 campos por um componente de entrada rápida de "Ano" e botão de inserção "+ Adicionar Ano à Tabela".
- [x] 1.2 Implementar handler de inclusão de ano que valida duplicatas locais e ativa a linha rascunho na tabela em modo de edição inline (`editingYear = ano`).

## 2. Edição Inline e Preview Dinâmico na Tabela

- [x] 2.1 Atualizar as colunas da tabela em modo de edição para exibir os previews formatados dos Tetos Bazin e Graham em tempo real conforme o usuário digita nos campos `dpa`, `lpa` e `vpa`.
- [x] 2.2 Ajustar o salvamento (`handleSaveEdit`) para tratar campos numéricos vazios como `null` ao invés de `NaN`, garantindo compatibilidade com o backend.
- [x] 2.3 Ajustar o cancelamento (`handleCancelEdit`) para remover a linha rascunho temporária se o ano ainda não tiver sido persistido no backend.

## 3. Validação e Testes

- [x] 3.1 Validar a interface no frontend adicionando um novo ano a um ativo (ex: PETR4) e preenchendo os valores diretamente na tabela.
- [x] 3.2 Verificar a atualização em tempo real dos Tetos Bazin e Graham na tabela durante a edição e confirmar a persistência dos dados salvos no backend.
