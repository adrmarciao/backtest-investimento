## Why

O sistema atual foca na simulação histórica de estratégias (Backtest), porém o investidor necessita de uma ferramenta operacional do presente para decidir **quanto e o que comprar** a cada semana ou mês, com base no método de Décio Bazin (Preço Teto Dividendos) e Benjamin Graham (Preço Justo). Atualmente, esse processo é realizado por meio de uma planilha Excel externa ("Ações Adriano.xlsx"), que calcula o rateio proporcional de aportes, retração Fibonacci e drawdown de mercado. Integrar essa funcionalidade em um menu dedicado ("Plano de Compras") proporciona uma experiência unificada, persistência centralizada no MongoDB, atualização automática de cotações via Google Finance e enriquecimento pontual de indicadores via Brapi.

## What Changes

- **Novo Menu Dedicado "Plano de Compras"**: Desacoplado do cadastro e simulação de backtest, voltado para a alocação de aportes da rodada corrente.
- **Configurações Globais de Aporte**:
  - Saldo Total, número de parcelas (meses) e seletor de periodicidade (`Semanal` ou `Mensal`).
  - Cálculo automático do valor da rodada (`Saldo / Meses` ou `Saldo / (Meses * 4)`), permitindo também valor avulso manual.
  - Termômetro de Mercado com seletor de Benchmark (`IBOV` / `IDIV`), cálculo de Drawdown e Retração Fibonacci (Alta do ano, Fibo Up e Fibo Down).
- **Cadastro e Gestão de Carteira de Compras**:
  - Cadastro persistido de ativos por ticker com setor, peso base, peso de habilitação (0.0 a 1.0) e ajuste manual de quantidade.
  - DPA Forecast (MarketScreener) preenchido manualmente.
- **Cotações e Fundamentos Multi-fonte**:
  - Busca automática das cotações em tempo real via **Google Finance** na abertura da tela.
  - Botão dedicado de **Preenchimento Automático via Brapi** para buscar LPA e VPA atualizados em lote utilizando o token do usuário.
  - Tabela totalmente editável inline, com salvamento em massa ao pressionar **Enter** ou clicar em **Salvar Alterações**.
- **Motor de Rateio e Boleta de Compra**:
  - Cálculo da proporção de peso efetivo de cada ação habilitada.
  - Determinação da quantidade inteira de ações a comprar e custo total por ativo.
  - Resumo de caixa (Total a Pagar, Sobra de Caixa, DY Médio Projetado) e botão para copiar a boleta de ordens.

## Capabilities

### New Capabilities
- `purchase-plan`: Gerenciamento do plano de compras periódicas, alocação de aportes, cálculo de boleta por Bazin/Graham, consulta de cotações Google Finance e sincronização de fundamentos Brapi.

### Modified Capabilities
- `app-navigation`: Inclusão do novo menu "Plano de Compras" na barra lateral de navegação.

## Impact

- **Backend**:
  - Novas entidades de domínio e persistência no MongoDB (`PurchasePlanConfig` e `PurchasePlanAsset`).
  - Adapters/Gateways para consulta de cotações no Google Finance e consulta de fundamentos na Brapi.
  - Controller REST (`PurchasePlanController`) para expor endpoints de configuração, listagem de ativos, recálculo e sincronização.
- **Frontend**:
  - Nova visualização/aba `PurchasePlanTab.jsx` no Material Design 3.
  - Integração no `Sidebar.jsx` e `App.jsx`.
  - Tabela editável com suporte a teclado (Enter), badges de margem de segurança e resumo de boleta.
