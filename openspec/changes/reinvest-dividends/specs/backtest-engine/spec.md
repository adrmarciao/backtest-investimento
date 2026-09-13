## ADDED Requirements

### Requirement: Acumular e gerenciar caixa de dividendos recebidos
O sistema SHALL identificar o pagamento de dividendos ao longo do tempo com base no número de cotas do ativo acumuladas até a data ex/pagamento e creditar o valor total (cotas × valor_dividendo_por_ação) no saldo em caixa de dividendos do portfólio.

#### Scenario: Dividendos recebidos e creditados no caixa
- **WHEN** ocorre uma data de pagamento/data-ex de dividendo para um ativo com cotas mantidas no portfólio
- **THEN** o sistema SHALL calcular o valor recebido (`cotas × dividendo_por_ação`) e adicionar ao saldo em caixa de dividendos acumulados

#### Scenario: Ativo sem cotas na data do dividendo
- **WHEN** um dividendo é distribuído, mas o portfólio possui zero cotas daquele ativo na data
- **THEN** o sistema SHALL ignorar a distribuição e não alterar o caixa de dividendos

### Requirement: Executar reinvestimento condicional de dividendos
O sistema SHALL tentar reinvestir o saldo acumulado no caixa de dividendos na próxima oportunidade de compra (ou data de pagamento), aplicando os mesmos critérios de compra da estratégia (critérios fixos P/L, P/VP, Dívida/EBITDA, ROE e preços-teto Bazin e Graham). Se os critérios de compra forem satisfeitos, o sistema compra o máximo de cotas possíveis com o caixa de dividendos. Se os critérios NÃO forem satisfeitos, o saldo de dividendos permanece retido no caixa e é somado para a próxima oportunidade.

#### Scenario: Reinvestimento com critérios satisfeitos
- **WHEN** há saldo positivo no caixa de dividendos e os critérios fundamentalistas e de preço-teto são satisfeitos na data
- **THEN** o sistema SHALL executar a compra de cotas adicionais usando o caixa de dividendos disponível e abater o valor gasto do caixa

#### Scenario: Reinvestimento impedido por critérios não satisfeitos
- **WHEN** há saldo positivo no caixa de dividendos, mas um ou mais critérios da estratégia (P/L, P/VP, Dív/EBITDA, ROE, Bazin ou Graham) falham na data
- **THEN** o sistema SHALL não executar compra de reinvestimento naquela data e acumular o saldo em caixa para a próxima verificação

#### Scenario: Saldo residual no caixa de dividendos
- **WHEN** o valor em caixa de dividendos é menor do que o preço de 1 cota inteira do ativo no momento do reinvestimento
- **THEN** o sistema SHALL comprar apenas o número máximo de cotas inteiras possíveis e manter o troco/saldo residual no caixa de dividendos
