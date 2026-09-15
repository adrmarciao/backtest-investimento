## 1. Backend: Modelo de Domínio e Persistência

- [x] 1.1 Criar entidades de domínio `PurchasePlanConfig` e `PurchasePlanAsset` no backend Java.
- [x] 1.2 Criar documentos MongoDB e repositories `PurchasePlanConfigMongoRepository` e `PurchasePlanAssetMongoRepository`.
- [x] 1.3 Implementar use cases para salvar/recuperar configurações globais de aporte e gerenciar a lista de ativos da carteira.

## 2. Backend: Integrações Externas e Cálculos

- [x] 2.1 Implementar client de cotações para Google Finance (obtenção de preços atuais por ticker e índices IBOV/IDIV).
- [x] 2.2 Implementar client para a API da Brapi (consulta em lote de LPA e VPA com token de autenticação).
- [x] 2.3 Implementar serviço de cálculo de rateio da rodada, sobras de caixa, Drawdown e níveis de retração Fibonacci.
- [x] 2.4 Criar `PurchasePlanController` com endpoints REST:
  - `GET /api/v1/purchase-plan/config` e `PUT /api/v1/purchase-plan/config`
  - `GET /api/v1/purchase-plan/assets` e `PUT /api/v1/purchase-plan/assets` (salvamento em lote)
  - `POST /api/v1/purchase-plan/sync-quotes` (Google Finance)
  - `POST /api/v1/purchase-plan/sync-fundamentals` (Brapi)

## 3. Frontend: Navegação e Componente Principal

- [x] 3.1 Adicionar item "Plano de Compras" na navegação lateral (`Sidebar.jsx`) e rota correspondente em `App.jsx`.
- [x] 3.2 Criar serviço de API no frontend (`src/services/purchasePlanService.js`) para comunicação com os endpoints do backend.
- [x] 3.3 Construir componente `PurchasePlanTab.jsx` estruturado no padrão Material Design 3.

## 4. Frontend: Cards de Orçamento, Termômetro Macro e Tabela Editável

- [x] 4.1 Implementar cards superiores de Orçamento (Saldo, Parcelas, Semanal/Mensal, Aporte Calculado/Manual) e Termômetro Macro (IBOV/IDIV, Drawdown e Fibonacci).
- [x] 4.2 Construir tabela editável com colunas de Ticker, Setor, Cotação, DPA Forecast, Teto Bazin, LPA, VPA, Graham, Margem %, Habilitação, Peso e Quantidades.
- [x] 4.3 Implementar botão "Preenchimento Automático (Brapi)" com diálogo/campo para configuração do token.
- [x] 4.4 Implementar atalho de teclado (Enter) e botão "Salvar Alterações" para persistência em lote.
- [x] 4.5 Implementar card de resumo da rodada (Total a Pagar, Sobra de Caixa, DY Médio) e botão "Copiar Boleta de Ordens".

## 5. Validação e Testes

- [x] 5.1 Testar carga inicial da tela com cotações automáticas do Google Finance.
- [x] 5.2 Testar preenchimento automático de LPA e VPA com token da Brapi.
- [x] 5.3 Validar os cálculos de Bazin, Graham, quantidades inteiras e rateio comparando com os valores da planilha original.
- [x] 5.4 Testar persistência das edições no MongoDB e recarregamento da tela.
