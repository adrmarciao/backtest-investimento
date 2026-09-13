## 1. Dados de Mercado e Proventos

- [x] 1.1 Implementar busca e cache do histórico de proventos/dividendos por ticker no serviço de dados de mercado (`yfinance`) e verificar o retorno dos proventos.

## 2. Motor de Backtest e Lógica de Reinvestimento

- [x] 2.1 Atualizar a linha do tempo do motor de backtest para identificar pagamento de proventos e creditar os valores no caixa de dividendos acumulados.
- [x] 2.2 Implementar a execução de compra via reinvestimento de dividendos condicionada ao cumprimento dos critérios da estratégia (P/L, P/VP, Dívida/EBITDA, ROE, Bazin e Graham).
- [x] 2.3 Garantir a retenção e acúmulo em caixa dos dividendos não investidos quando os critérios de compra não forem satisfeitos no período.

## 3. Métricas e Resposta de API

- [x] 3.1 Atualizar o retorno do backend para incluir estatísticas de proventos: total recebido, total reinvestido e saldo remanescente em caixa.

## 4. Frontend e Validação

- [x] 4.1 Exibir os cards de métricas de dividendos e destacar as compras de reinvestimento na interface React.
- [x] 4.2 Executar a simulação de backtest com a opção de reinvestimento de dividendos e validar o funcionamento completo.

