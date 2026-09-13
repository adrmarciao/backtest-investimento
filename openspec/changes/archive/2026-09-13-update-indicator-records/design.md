## Context

Ver `proposal.md - Why` para motivação. O backend Spring Boot já fornece o endpoint REST `PUT /api/v1/assets/{ticker}/indicators/{year}` e o repositório MongoDB já suporta a atualização por ID composto `TICKER_ANO`. O cliente de API no frontend (`api.js`) já possui a função `updateIndicators(ticker, year, data)`. A alteração necessária é focada no componente React `IndicatorsTab.jsx`.

## Goals / Non-Goals

**Goals:**
- Implementar estado local no React (`editingYear` e `editForm`) no componente `IndicatorsTab.jsx` para controlar a linha em modo de edição.
- Renderizar inputs numéricos estilizados nas próprias células da linha da tabela do "Histórico Fundamentalista Cadastrado" durante a edição.
- Recalcular dinamicamente os valores de Teto Bazin e Teto Graham na mesma linha conforme o usuário altera os campos DPA, LPA ou VPA.
- Executar a requisição `updateIndicators`, recarregar a lista e sair do modo de edição ao clicar no botão de confirmação (✓).
- Permitir cancelar a edição (✕) para descartar alterações pendentes sem alterar os dados.

**Non-Goals:**
- Não criar modais ou redirecionar o usuário para outras telas.
- Não alterar a estrutura do banco de dados MongoDB ou dos controladores Spring Boot.

## Decisions

### Decisão 1: Edição Inline na Tabela via Estado Local no React
- **Opção Escolhida**: Manter o estado da linha editável (`editingYear` e objeto `editForm`) no `IndicatorsTab.jsx`.
- **Motivação**: Atende exatamente ao requisito do usuário de alterar dados diretamente no Histórico Fundamentalista Cadastrado na própria listagem com um clique no check (✓).
- **Alternativas Consideradas**: Modal de edição ou redirecionamento de tela (descartados conforme alinhado com o usuário).

### Decisão 2: Reutilização da função `updateIndicators` da camada `api.js`
- **Opção Escolhida**: Chamar `updateIndicators(selectedTicker, editingYear, editForm)` ao confirmar.
- **Motivação**: A camada de serviço HTTP já possui o método `PUT` pronto.

## Risks / Trade-offs

- **[Risco]** Inputs ocupando muito espaço na tabela em telas pequenas → *Mitigação*: Aplicar classe CSS com largura compacta (~70px) e padding adequado nos inputs inline da tabela.
