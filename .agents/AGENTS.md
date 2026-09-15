# AGENTS.md - Diretrizes do Projeto

## Git & OpenSpec Workflow
- **Criação de Branch por Spec/Change**: Ao criar ou propor uma nova mudança/spec do OpenSpec (`openspec/changes/<change-name>`), crie automaticamente uma branch git dedicada com o nome da mudança:
  `git checkout -b <change-name>`
- **Commit e Push Pós-Arquivamento**: Após o arquivamento de uma mudança do OpenSpec (`/opsx-archive`), faça automaticamente o commit das alterações realizadas e o push da branch dedicada para o repositório remoto.

## Agentes
- **Uso do Agente Frontend**: Ao codificar partes/funcionalidades de frontend no projeto, use sempre o agente frontend. Não carregue o agente desnecessariamente para tarefas fora desse escopo.
- **Uso do Agente Backend**: Ao codificar partes/funcionalidades de backend (Java/Spring Boot/MongoDB/APIs) no projeto, use sempre o agente backend. Não carregue o agente desnecessariamente para tarefas fora desse escopo.
- **Delegação Pós-Arquivamento**: Após o arquivamento, delegar e notificar o agente especialista (Frontend ou Backend) responsável pela implementação das tarefas concluídas.


