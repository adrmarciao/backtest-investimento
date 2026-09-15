## 1. Backend Implementation

- [x] 1.1 Atualizar DTO `BacktestRequest` no `BacktestController.java` para adicionar o campo `List<String> tickers` e os métodos getter/setter
- [x] 1.2 Atualizar a interface `ExecuteBacktestPort` e a classe `ExecuteBacktestUseCase` para aceitar a lista de `tickers`, aplicando a filtragem de ativos caso a lista não seja nula ou vazia
- [x] 1.3 Criar/atualizar testes unitários no backend para validar o comportamento de filtragem por tickers no `ExecuteBacktestUseCaseTest`

## 2. Frontend Implementation

- [x] 2.1 Atualizar a função `executeBacktest` no `frontend/src/services/api.js` para aceitar a lista de `tickers` e enviá-la no corpo do `POST /api/v1/backtest`
- [x] 2.2 Atualizar `BacktestTab.jsx` para buscar a lista de ativos cadastrados via `getAssets()` no carregamento do componente e inicializar `selectedTickers` com todos os tickers
- [x] 2.3 Adicionar o componente `Autocomplete` MUI multi-seleção com Chips dos tickers e os botões utilitários "Selecionar Todos" e "Limpar Seleção" em `BacktestTab.jsx`
- [x] 2.4 Adicionar validação de interface para desabilitar o botão de submissão e exibir um alerta caso nenhum ticker esteja selecionado

## 3. Verification

- [x] 3.1 Executar `mvn test` na pasta `backend` e garantir que todos os testes unitários da aplicação passem com sucesso
- [x] 3.2 Executar a simulação na interface selecionando tickers específicos e verificar que apenas os ativos selecionados são processados no resultado

