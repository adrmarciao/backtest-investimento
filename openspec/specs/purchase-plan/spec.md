# purchase-plan Spec

## Purpose

Fornecer uma interface operacional de suporte à compra periódica de ações, permitindo definir saldo a investir, periodicidade (semanal/mensal), rateio por pesos e margem de segurança fundamentalista (Bazin e Graham), com cotações automáticas via Google Finance, enriquecimento sob demanda via Brapi e controle manual total.

## Requirements

### Requirement: Configuração do Orçamento e Periodicidade de Aporte
O sistema SHALL permitir que o usuário configure o saldo total a investir, a quantidade de parcelas (em meses) e a frequência dos aportes (semanal ou mensal).

#### Scenario: Cálculo automático da parcela mensal
- **WHEN** o usuário seleciona periodicidade mensal com saldo de R$ 60.000 e 12 parcelas
- **THEN** o sistema SHALL calcular o aporte da rodada como R$ 5.000,00

#### Scenario: Cálculo automático da parcela semanal
- **WHEN** o usuário seleciona periodicidade semanal com saldo de R$ 77.702 e 12 parcelas
- **THEN** o sistema SHALL calcular o aporte da rodada como R$ 1.618,79 (Saldo / Parcelas / 4)

#### Scenario: Sobrescrita manual do aporte da rodada
- **WHEN** o usuário informa um valor manual para o aporte da rodada
- **THEN** o sistema SHALL utilizar esse valor manual no rateio de compras em substituição ao cálculo padrão

---

### Requirement: Termômetro Macro de Mercado (Fibonacci & Drawdown)
O sistema SHALL exibir o status de retração do mercado com seletor de benchmark entre IBOV e IDIV.

#### Scenario: Seleção de Benchmark e exibição de métricas
- **WHEN** o usuário seleciona IBOV ou IDIV
- **THEN** o sistema SHALL exibir cotação atual do índice, máxima do ano, drawdown percentual em relação à máxima e o nível de retração Fibonacci

---

### Requirement: Cadastro e Persistência de Ativos da Carteira de Compras
O sistema SHALL persistir a lista de ativos monitorados para compra no MongoDB de forma independente do cadastro do módulo de Backtest.

#### Scenario: Adição de novo ativo
- **WHEN** o usuário cadastra um novo ticker com setor, peso inicial e DPA forecast
- **THEN** o sistema SHALL salvar o ativo na coleção de compras com ordem de exibição configurável

#### Scenario: Edição inline e confirmação via teclado ou botão
- **WHEN** o usuário edita valores nas células da tabela e pressiona a tecla Enter ou clica em Salvar
- **THEN** o sistema SHALL persistir todas as alterações no MongoDB

---

### Requirement: Cotações Automáticas via Google Finance
O sistema SHALL buscar a cotação de mercado mais recente das ações no Google Finance automaticamente ao carregar a tela.

#### Scenario: Carregamento inicial da tela
- **WHEN** a tela de Plano de Compras é aberta
- **THEN** o sistema SHALL consultar as cotações atuais dos ativos monitorados no Google Finance e preencher a coluna de Preço Atual mantendo o campo editável

---

### Requirement: Preenchimento Automático de Fundamentos via Brapi
O sistema SHALL consultar a API da Brapi sob demanda para atualizar LPA e VPA utilizando o token configurado pelo usuário.

#### Scenario: Acionamento do botão de preenchimento automático
- **WHEN** o usuário aciona o botão de sincronização Brapi
- **THEN** o sistema SHALL realizar a consulta em lote na Brapi e preencher os campos de LPA e VPA dos ativos correspondentes

---

### Requirement: Rateio de Compras e Geração da Boleta
O sistema SHALL calcular o Preço Teto Bazin, Preço Graham, Margem de Segurança e a quantidade de ações a comprar baseando-se no rateio proporcional dos pesos habilitados.

#### Scenario: Cálculo de quantidades por lote
- **WHEN** o valor da rodada é distribuído entre os ativos habilitados
- **THEN** o sistema SHALL calcular a quantidade inteira de ações através de `floor(Valor Alocado / Preço Atual) + Ajuste Manual`
- **AND** SHALL totalizar o valor a pagar, a sobra de caixa e permitir a cópia formatada da boleta de compras
