## Context

O investidor adota a metodologia de acumulação de capital com aportes periódicos na B3. A decisão de alocação utiliza o Preço Teto de Décio Bazin (`DPA / 0.06`) e o Preço Justo de Benjamin Graham (`sqrt(22.5 * LPA * VPA)`). Atualmente, esses cálculos eram mantidos em uma planilha Excel ("Ações Adriano.xlsx"). O objetivo é migrar essa inteligência para o sistema, mantendo o controle manual e introduzindo integrações com Google Finance e Brapi.

## Goals / Non-Goals

**Goals:**
- Criar um módulo independente ("Plano de Compras") com navegação dedicada na barra lateral.
- Persistir as configurações de aporte e a carteira monitorada no MongoDB.
- Buscar cotações atuais no Google Finance na inicialização da página.
- Disponibilizar botão para enriquecimento automático de LPA e VPA via API Brapi (usando token do usuário).
- Fornecer tabela editável rápida (com suporte a tecla Enter para salvar tudo).
- Calcular quantidades de compra inteiras, sobras de caixa e métricas de retração Fibonacci/Drawdown do IBOV/IDIV.
- Manter total separação das entidades e tabelas do módulo de Backtest Histórico.

**Non-Goals:**
- Misturar ou sincronizar a lista de ativos de compra com os ativos do backtest histórico.
- Enviar ordens reais de compra diretamente para corretoras (a boleta é gerada para conferência e cópia manual).
- Executar backtest temporal dentro da tela de plano de compras.

## Decisions

### Decisão 1: Separação de Domínio (Bounded Context)
- **Escolha**: Criar novas entidades `PurchasePlanConfig` e `PurchasePlanAsset` isoladas de `Asset` e `AnnualIndicators`.
- **Racional**: O cadastro do backtest contém séries temporais anuais e regras de simulação passada, enquanto o plano de compras é operacional, pontual e contém forecasts futuros (DPA) e pesos de rodada.

### Decisão 2: Google Finance para Cotações (Ao Abrir a Tela)
- **Escolha**: O backend provê um serviço que consulta o Google Finance para obter a cotação instantânea de cada ticker (`/quote/{ticker}:BVMF`).
- **Racional**: Não consome limites de cota da Brapi e reflete o preço que o usuário já acompanhava na planilha.

### Decisão 3: Brapi para LPA e VPA Sob Demanda
- **Escolha**: A consulta ao Brapi (`/api/quote/{tickers}?fundamental=true`) ocorre estritamente ao clicar no botão "Preenchimento Automático".
- **Racional**: Como LPA e VPA mudam apenas trimestralmente, a busca sob demanda economiza a cota de requisições da chave do usuário.

### Decisão 4: UX com Tabela Editável e Confirmação por Enter
- **Escolha**: Células com inputs diretos ou click-to-edit. Ao teclar Enter ou clicar em Salvar, o estado completo da tabela é enviado em um único payload para o backend.

## Risks / Trade-offs

- **[Risco]** Mudança de markup no Google Finance pode afetar a extração da cotação.
  → *Mitigação*: Fallback gracioso onde o preço anterior/persistido é mantido e o usuário pode editar o valor manualmente a qualquer momento.
- **[Risco]** Falha de rede ou token inválido na Brapi.
  → *Mitigação*: Feedback visual no frontend com alerta claro e permissão para digitação manual direta de LPA e VPA.
