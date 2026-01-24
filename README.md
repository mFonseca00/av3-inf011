<h1 align="center"> Questão 1 </h1>

## Porque decidimos usar Strategy nesse contexto

Como esse sistema de gerenciamento de documentos tem diferentes tipos de documentos, é preciso que sejam possíveis diferentes formas de autenticação. Por exemplo:

* Documentos criminais precisam de um código que inclua o ano
* Documentos pessoais precisam de um código com o dia do ano e identificação do proprietário
* Documentos para exportação precisam verificar se são sigilosos ou públicos
* Documentos gerais precisam apenas de um código simples com timestamp

Sem o uso do Strategy, teríamos que usar múltiplos if-else ou switches dentro da classe Autenticador, violando o Princípio Aberto/Fechado (OCP). Com o Strategy, podemos adicionar mais estratégias de autenticação modificando minimamente o código que já existe.

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
| Context**        | `Autenticador`         | Usa a estratégia e permite sua troca em tempo de execução |
| ConcreteStrategy | `DefaultAuthStrategy`  | Gera códigos padrão com timestamp                         |
| ConcreteStrategy | `CriminalAuthStrategy` | Gera códigos para documentos criminais                    |
| ConcreteStrategy | `PessoalAuthStrategy`  | Gera códigos para documentos pessoais                     |
| ConcreteStrategy | `ExportAuthStrategy`   | Gera códigos baseados na privacidade                      |

<h1 align="center"> Questão 2 </h1>
