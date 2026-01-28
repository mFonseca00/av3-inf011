<h1 align="center"> Questão 1 </h1>

## Porque decidimos usar Strategy nesse contexto

Como esse sistema de gerenciamento de documentos tem diferentes tipos de documentos, é preciso que sejam possíveis diferentes formas de autenticação. Por exemplo:

* Documentos criminais precisam de um código que inclua o ano
* Documentos pessoais precisam de um código com o dia do ano e identificação do proprietário
* Documentos para exportação precisam verificar se são sigilosos ou públicos
* Documentos gerais precisam apenas de um código simples com timestamp

Sem o uso do Strategy, teríamos que usar múltiplos if-else ou switches dentro da classe Autenticador, violando o Princípio Aberto/Fechado (OCP). Com o Strategy, podemos adicionar mais estratégias de autenticação, modificando minimamente o código que já existe.

## Estrutura do Padrão

O padrão Strategy possui uma estrutura composta da seguinte forma: 

Context (Que no nosso código é a classe Autenticador) que usa a Interface de Strategy (Que no nosso código é representado por AuthStrategy) e essa interface é implementada pela Concrete Strategy (Que no nosso código são as classes DefaultAuthStrategy, CriminalAuthStrategy, ExportAuthStrategy, PessoalAuthStrategy). Por fim, alteramos o Client (Que no nosso código é o GerenciadorDocumentoModel) para inicializar um hashmap responsável de armazenar os indexes para a escolha da estratégia e chamar o autenticador, que seta a estratégia e autentica. Caso seja necessário adicionar uma nova estrategia, basta criá-la e adicionar ao map, tornando o código expansível.

## Fluxo de Funcionamento
```text
Cliente (GerenciadorDocumentoModel) cria um Autenticador (Context)
              │
              ▼
Cliente define a estratégia desejada (opcional)
autenticador.setStrategy(new CriminalAuthStrategy())
              │
              ▼
Cliente chama autenticar(documento)
              │
              ▼
Autenticador delega para 
strategy.generateAuthCode(documento)
              │
              ▼
Strategy concreta gera o código específico
              │
              ▼
Código é atribuído ao documento

```
## Resumo dos Papéis

| Componente       |        Classe          |                  Responsabilidade                         |
|------------------|------------------------|-----------------------------------------------------------|
| Strategy         | `AuthStrategy`         | Interface que define o contrato para geração de códigos   |
| Context          | `Autenticador`         | Usa a estratégia e permite sua troca em tempo de execução |
| ConcreteStrategy | `DefaultAuthStrategy`  | Gera códigos padrão com timestamp                         |
| ConcreteStrategy | `CriminalAuthStrategy` | Gera códigos para documentos criminais                    |
| ConcreteStrategy | `PessoalAuthStrategy`  | Gera códigos para documentos pessoais                     |
| ConcreteStrategy | `ExportAuthStrategy`   | Gera códigos baseados na privacidade                      |

<h1 align="center"> Questão 2 </h1>

## Porque decidimos usar Command nesse contexto

Com o crescimento do volume de documentos no framework, os usuários passaram a demandar maior produtividade e segurança. O Conselho de Gestão de TI estabeleceu novos requisitos arquiteturais obrigatórios:

* Log de todas as operações em arquivo para auditoria
* Reversibilidade universal (Undo) de qualquer operação realizada sobre documentos
* Lista de refazer (Redo) para operações desfeitas por engano
* Operações compostas (Macros) que executam sequências de ações como um único bloco
* Operação de consolidar que limpa o histórico, confirmando alterações permanentemente

Sem o uso do Command, teríamos que implementar lógica de undo/redo e logging diretamente em cada método que altera documentos (assinar, proteger, setConteudo), resultando em código duplicado e violando o Princípio da Responsabilidade Única (SRP). Com o Command, encapsulamos cada operação como um objeto independente, permitindo parametrização, enfileiramento, registro de histórico e reversibilidade uniforme.

## Estrutura do Padrão

O padrão Command possui uma estrutura composta da seguinte forma:

Command (Que no nosso código é a interface Command) que define o contrato para operações reversíveis (execute, undo, getLogInfo). Essa interface é implementada pelos Concrete Commands (Que no nosso código são as classes CriarDocCommand, SalvarCommand, AssinarCommand, ProtegerCommand, TornarUrgenteCommand e MacroCommand). O Receiver (Que no nosso código é o DocumentHolder) mantém a referência ao documento sendo manipulado. O Invoker (Que no nosso código é o CommandManager) gerencia a execução dos comandos, o histórico de undo/redo e o logging. Por fim, o Client (Que no nosso código é o GerenciadorDocumentoModel) cria os comandos concretos e delega sua execução ao CommandManager. Caso seja necessário adicionar uma nova operação, basta criar um novo Command e passá-lo ao CommandManager, mantendo o código expansível e desacoplado.

## Fluxo de Funcionamento

```text
Cliente (GerenciadorDocumentoModel)
              │
              ▼
Cria um Command concreto
(ex: SalvarCommand, AssinarCommand)
              │
              ▼
Passa o comando para CommandManager
commandManager.executeCommand(comando)
              │
              ▼
CommandManager executa o comando
comando.execute()
              │
              ├─────────────────────────┐
              ▼                         ▼
    Comando altera estado      Comando é armazenado
    (via DocumentHolder)        na pilha de undo
              │                         │
              ▼                         ▼
    CommandManager registra      Pilha de redo é limpa
    operação em log
              │
              ▼
    operations.log atualizado
```

### Fluxo de Redo

```text
Cliente solicita redo
              │
              ▼
CommandManager.redo()
              │
              ▼
Remove comando da pilha de redo
              │
              ▼
Executa comando.execute()
              │
              ▼
Atualiza estado do documento
              │
              ▼
Move comando para pilha de undo
              │
              ▼
Registra operação em log
```

### Fluxo de Undo

```text
Cliente solicita undo
              │
              ▼
CommandManager.undo()
              │
              ▼
Remove comando da pilha de undo
              │
              ▼
Executa comando.undo()
              │
              ▼
Restaura estado anterior do documento
              │
              ▼
Move comando para pilha de redo
              │
              ▼
Registra operação em log
```

### Fluxo de MacroCommand (Composite)

```text
Cliente cria MacroCommand
              │
              ▼
Adiciona comandos individuais
macro.adicionarComando(cmd1)
macro.adicionarComando(cmd2)
              │
              ▼
CommandManager executa macro
              │
              ▼
MacroCommand executa cada comando
sequencialmente
              │
              ▼
Se undo() for chamado, comandos
são desfeitos em ordem reversa
```

## Resumo dos Papéis

| Componente        | Classe/Interface          | Responsabilidade                                                      |
|-------------------|---------------------------|-----------------------------------------------------------------------|
| Command           | `Command`                 | Interface que define contrato para operações reversíveis              |
| ConcreteCommand   | `CriarDocCommand`         | Encapsula criação de documento                                        |
| ConcreteCommand   | `SalvarCommand`           | Encapsula alteração de conteúdo                                       |
| ConcreteCommand   | `AssinarCommand`          | Encapsula assinatura                                                  |
| ConcreteCommand   | `ProtegerCommand`         | Encapsula proteção via proxy                                          |
| ConcreteCommand   | `TornarUrgenteCommand`    | Encapsula marcação de urgência                                        |
| ConcreteCommand   | `MacroCommand`            | Encapsula sequência de comandos (Composite)                           |
| Receiver          | `DocumentHolder`          | Mantém referência ao documento sendo manipulado                       |
| Invoker           | `CommandManager`          | Gerencia execução, histórico (undo/redo) e logging                    |
| Client            | `GerenciadorDocumentoModel` | Cria comandos e delega execução ao CommandManager                   |

## Log de Operações

Todas as operações executadas pelo sistema são registradas automaticamente no arquivo `operations.log` na raiz do projeto. Esse log serve para auditoria e rastreabilidade das ações realizadas sobre os documentos.

Exemplo de linha registrada no log:

```
[2026-01-28 14:30:15] EXECUTE - Criar documento - Privacidade: PUBLICO
```
