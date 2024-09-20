# Alumind - Microsserviço de Análise de Feedbacks

## Descrição do Projeto

Este projeto é um protótipo de uma aplicação web para a **Alumind**, uma startup focada em bem-estar e saúde mental. A aplicação tem como objetivo analisar os feedbacks dos usuários, classificá-los de acordo com seu sentimento, sugerir possíveis funcionalidades a partir desses feedbacks e gerar uma resposta automática personalizada para cada feedback. Além disso, a aplicação possui detecção de SPAM.

## Tecnologias Utilizadas

- **Java 21**: Linguagem principal do projeto.

- **Spring Boot**: Framework utilizado para simplificar o desenvolvimento da aplicação.

- **Spring Data JPA**: Utilizado para persistência de dados no banco de dados.

- **MySQL**: Banco de dados relacional utilizado.

- **LLM (OpenAI GPT-4)**: Utilizado para análise de sentimentos, processamento do feedback, geração de respostas automáticas e personalizadas e detecção de SPAM nos feedbacks.

- **Git & GitHub**: Versionamento de código e repositório remoto.

## Funcionalidades Implementadas


### 1. Classificação de Feedbacks

A aplicação possui um **endpoint** que recebe feedbacks de usuários, analisa o sentimento (POSITIVO, NEGATIVO ou INCONCLUSIVO), identifica funcionalidades sugeridas no texto e gera uma resposta automática personalizada para cada feedback.

- **Endpoint**: `POST /feedbacks`

- **Exemplo de Requisição**:

`POST /feedbacks`
`Content-Type: application/json`

```json 
{
	"feedback": "Gostei muito do Alumind, mas gostaria de poder editar meu perfil mais facilmente."
}
```

- **Exemplo de Resposta**:

```json 
{
	"id": 1,
	"sentiment": "POSITIVO",
	"requested_features": [
		{
			"code": "EDITAR_PERFIL",
			"reason": "O usuário gostaria de realizar a edição do próprio perfil"
		}
	]
}
```

### 2. Detecção de SPAM

Para evitar que feedbacks indesejados (como SPAM) sejam processados, a aplicação usa um modelo de linguagem (LLM) para detectar automaticamente feedbacks agressivos ou irrelevantes.

- Feedbacks que forem classificados como SPAM são ignorados, e o endpoint retorna um status `204 No Content` sem salvar os dados.

### 3. Geração de Respostas Personalizadas

A aplicação gera respostas automáticas e personalizadas para cada feedback, considerando o sentimento detectado e as funcionalidades sugeridas.

- **Exemplo de Resposta Automática**:

`GET /feedbacks`

```json 
{
	"id": 1,
	"feedback": "Gostei muito do Alumind, mas gostaria de poder editar meu perfil mais facilmente.",
	"sentiment": "POSITIVO",
	"custom_response": "Obrigado pelo seu feedback positivo! Ficamos felizes em saber que você está gostando do Alumind. Vamos considerar sua sugestão de facilitar a edição do perfil para futuras atualizações.",
	"requested_features": [
		{
			"code": "EDITAR_PERFIL",
			"reason": "O usuário gostaria de realizar a edição do próprio perfil"
		}
	]
}
```

### 4. Validação de Funcionalidades Sugeridas

Cada funcionalidade sugerida tem um código que a identifica unicamente, garantindo que sugestões duplicadas possam ser agrupadas. O código é armazenado em uma entidade separada no banco de dados.

## Exemplos de Requisições e Respostas da API de Feedbacks

>**Obs.:**:  Estes são apenas alguns exemplos, vários outros testes foram realizados para validar os retornos da API.

### Exemplo 1:
- **Requisição**:
```json
{
  "feedback": "Este app é muito bom, mas gostaria que houvessem mais aulas de meditação, por isso darei 4 estrelas."
}
```

- **Resposta Completa**:
```json
{
    "id": 1,
    "feedback": "Este app é muito bom, mas gostaria que houvessem mais aulas de meditação, por isso darei 4 estrelas.",
    "sentiment": "POSITIVO",
    "custom_response": "Obrigado pelo seu feedback positivo! Ficamos felizes em saber que você está gostando do nosso app. Vamos considerar sua sugestão de adicionar mais aulas de meditação para futuras atualizações.",
    "requested_features": [
      {
        "code": "MAIS_AULAS_MEDITACAO",
        "reason": "O usuário gostaria que houvessem mais aulas de meditação disponíveis no aplicativo"
      }
    ]
  }
```

### Exemplo 2:
- **Requisição**:
```json
{
  "feedback": "O aplicativo é horrível, trava demais e não tem disponível pra iOS. Minha esposa até queria usar, mas ela usa Iphone e não pode nem testar o aplicativo."
}
```

- **Resposta Completa**:
```json
{
    "id": 2,
    "feedback": "O aplicativo é horrível, trava demais e não tem disponível pra iOS. Minha esposa até queria usar, mas ela usa Iphone e não pode nem testar o aplicativo.",
    "sentiment": "NEGATIVO",
    "custom_response": "Sentimos muito por sua experiência negativa com nosso aplicativo. Estamos trabalhando para melhorar o desempenho e resolver os problemas de travamento. Também entendemos a importância de disponibilizar nossa aplicação para iOS e estamos buscando formas de atender essa necessidade. Agradecemos pelo seu feedback e esperamos proporcionar uma experiência melhor em breve.",
    "requested_features": [
      {
        "code": "MELHORAR_DESEMPENHO",
        "reason": "O usuário gostaria que o aplicativo parasse de travar com frequência"
      },
      {
        "code": "SUPORTE_IOS",
        "reason": "O usuário quer que o aplicativo esteja disponível para iOS para que sua esposa possa utilizá-lo"
      }
    ]
  }
```

### Exemplo 3:
- **Requisição**:
```json
{
  "feedback": "A moto é muito boa. Econômica e exige pouca manutenção, com certeza, recomendo. 5 Estrelas"
}
```
- **Resposta Completa**:
`204 No Content`

### Exemplo 4:
- **Requisição**:
```json
{
  "feedback": "O aplicativo é espetacular, adorei demais! É realmente sensacional, INCRÍVEL!!!!!! Só senti falta de uma opção para favoritar exercícios, compartilhar lista de exercícios pelas redes sociais e uma opção para poder editar a foto de perfil. Fora isso, é 5 estrelas!!!!!"
}
```

- **Resposta Completa**:
```json
{
    "id": 3,
    "feedback": "O aplicativo é espetacular, adorei demais! É realmente sensacional, INCRÍVEL!!!!!! Só senti falta de uma opção para favoritar exercícios, compartilhar lista de exercícios pelas redes sociais e uma opção para poder editar a foto de perfil. Fora isso, é 5 estrelas!!!!!",
    "sentiment": "POSITIVO",
    "custom_response": "Obrigado pelo seu feedback positivo! Ficamos muito felizes em saber que você adorou o aplicativo. Agradecemos pelas sugestões de favoritar exercícios, compartilhar a lista de exercícios e editar a foto de perfil. Vamos considerá-las para futuras atualizações.",
    "requested_features": [
      {
        "code": "FAVORITAR_EXERCICIOS",
        "reason": "O usuário gostaria de uma opção para favoritar exercícios"
      },
      {
        "code": "COMPARTILHAR_EXERCICIOS",
        "reason": "O usuário gostaria de compartilhar a lista de exercícios pelas redes sociais"
      },
      {
        "code": "EDITAR_FOTO_PERFIL",
        "reason": "O usuário gostaria de uma opção para editar a foto de perfil"
      }
    ]
  }
```

### Exemplo 5:
- **Requisição**:
```json
{
  "feedback": "Ahhhhhh! Achei o aplicativo bem mais ou menos"
}
```

- **Resposta Completa**:
```json
{
    "id": 4,
    "feedback": "Ahhhhhh! Achei o aplicativo bem mais ou menos",
    "sentiment": "NEGATIVO",
    "custom_response": "Obrigado pelo seu feedback. Lamentamos que a sua experiência com o aplicativo tenha sido apenas mediana. Agradecemos seu comentário e estamos sempre trabalhando para melhorar nosso serviço.",
    "requested_features": []
  }
```

## Como Executar o Projeto

>**Obs.:** É recomendado executar a aplicação em uma IDE como o Eclipse (Spring Tools Suite) ou IntelliJ IDEA.

### Pré-requisitos

1. **Java 21+**: Certifique-se de ter o Java 21 ou mais recente instalado.

2. **Banco de Dados**: É necessário ter acesso a um servidor MySQL para armazenar as entidades.

3. **OpenAI API Key**: É preciso uma chave de API da OpenAI para a análise dos feedbacks e detecção de SPAM.

### Passos para Configuração

1. **Clone o Repositório**:

`https://github.com/LucasHenriqueLS/feedbacks-api.git`

2. **Configuração do Banco de Dados e da API da OpenAI**: Atualize o arquivo `application.yml` com suas credenciais de banco de dados e sua chave de API da OpenAI (substitua `${ ... }` pelo respectivo valor ou adicione `: valor_do_campo` após o nome do campo, da mesma forma que na configuração da porta).
```yml
server:
  port: ${PORT:10000}

spring:
  datasource:
    url: jdbc:mysql://${MYSQL_IP}:3306/alumind_feedbacks?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true
    username: ${MYSQL_USERNAME}
    password: ${MYSQL_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.MySQLDialect

openai_api_key: ${OPENAI_API_KEY}
```
Ou configure as seguintes variáveis de ambiente:
```
MYSQL_IP=ip_do_mysql
MYSQL_PASSWORD=senha_do_mysql
MYSQL_USERNAME=usuario_do_mysql
OPENAI_API_KEY=sua_chave_de_api_da_openai
```
>**Obs.:** O projeto foi implementado de forma que o banco e as tabelas fossem gerados automaticamente, com o intuito de simplificar a execução e a avaliação do projeto.

4. **Executar a Aplicação**: Com o **Maven** ou **Gradle**, execute a aplicação localmente:

- **Linux**:
`./mvnw spring-boot:run`

- **Windows**:
`mvn spring-boot:run`


6. **Acessar a API**: A aplicação estará disponível em `http://localhost:10000`.

## Detalhes de Implementação

### Decisões Técnicas

#### Geração de Respostas Personalizadas

Optou-se por integrar a geração de respostas personalizadas diretamente no endpoint `/feedbacks`, porque:

- Durante o processamento do feedback, já temos todas as informações necessárias para gerar uma resposta personalizada (sentimento e funcionalidades sugeridas). Assim, evitamos duplicidade de chamadas a API da OpenAI e mantemos o fluxo de processamento eficiente. Além disso, cada feedback que não for considerado um SPAM deve receber uma resposta, o que justifica realizar todo o processamento em uma única chamada a API.

- Em vez de criar um novo endpoint dedicado apenas à geração de respostas, centralizamos todo o processamento em uma única chamada, evitando criar camadas adicionais de lógica, o que reduz a sobrecarga no sistema, simplifica a manutenção do código e o torna mais simples de entender e testar.

#### Implementação Seguindo Técnicas e Boas Práticas de Engenharia de Prompts

Na implementação do projeto, várias técnicas e boas práticas de engenharia de prompt foram empregadas para garantir uma resposta estruturada e específica do modelo de LLM. Foi utilizada a [documentação da OpenAI sobre engenharia de prompts](https://platform.openai.com/docs/guides/prompt-engineering) como referência.

- **Para a tarefa de analisar os feedback, foi elaborado o seguinte prompt**:

```
private String getAnalyzeFeedbackContent(String feedback, Set<String> functionalityCodes) {
    return """
            Através deste feedback:
            "%s"

            Monte o seguinte Json:
            {
                "sentiment": (( Determine se o sentimento do usuário no feedback é "POSITIVO" (Se o feedback conter elogios ou satisfação), "NEGATIVO" (Se o feedback incluir reclamações ou frustrações) ou "INCONCLUSIVO" (Se o sentimento não for claro ou for neutro) e atribua o valor apropriado no campo sentiment )),
                "requested_features": (( O feedback pode conter uma ou mais funcionalidades sugeridas. Identifique-as, caso haja, e forneça um código (escolha entre as opções em uma lista fornecida: %s. Caso nenhum dos códigos fornecidos seja apropriado, elabore um novo código seguindo os padrões, como "MELHORAR_DESEMPENHO". O código deve ser curto, descritivo e em letras maiúsculas) e uma descrição do porquê a funcionalidade é importante. Se o feedback não contiver nenhuma sugestão de funcionalidade ou se for muito vago, retorne uma lista vazia no JSON ))
                    [
                        {
                            "code": (( Por exemplo: "MELHORAR_DESEMPENHO" )),
                            "reason": (( Por exemplo: "O usuário gostaria de que a tela inicial carregasse mais rápido" ))
                        }
                    ]
                "custom_response": (( Você deve gerar uma resposta personalizada para o feedback, criando uma mensagem a partir do sentimento identificado e das possíveis melhorias propostas no feedback. Por exemplo: "Obrigado pelo seu feedback positivo! Ficamos felizes em saber que você está gostando do Alumind. Vamos considerar sua sugestão de facilitar a edição do perfil para futuras atualizações." )) 
            }

            Um exemplo de Json é o seguinte:
            {
                "sentiment": "POSITIVO",
                "requested_features": [
                    {
                        "code": "EDITAR_PERFIL",
                        "reason": "O usuário gostaria de realizar a edição do próprio perfil"
                    }
                ],
                "custom_response": "Obrigado pelo seu feedback positivo! Ficamos felizes em saber que você está gostando do Alumind. Vamos considerar sua sugestão de facilitar a edição do perfil para futuras atualizações."
            }
            
            A resposta deve ser um JSON válido, sem nenhum texto adicional além do JSON.
           """.formatted(feedback, String.join(", ", functionalityCodes));
}
```

As práticas utilizadas no prompt acima são detalhadas a seguir:

- **Instruções claras e específicas**: O prompt define claramente o que é esperado em cada campo do JSON, como o sentimento do feedback, as funcionalidades solicitadas e a geração de uma resposta personalizada. Isso ajuda o modelo a entender exatamente o que precisa fazer, reduzindo ambiguidade.

- **Clareza nas categorias de sentimento**: A instrução para determinar o "sentimento" é reforçada com uma descrição mais explícita dos critérios para cada sentimento. Isso ajuda o modelo a tomar decisões mais consistentes sobre o sentimento.

- **Listagem de códigos de funcionalidade**: É fornecida uma lista de códigos de funcionalidade, orientando o modelo sobre as opções disponíveis, reduzindo a ambiguidade. Caso nenhum código existente seja apropriado, são fornecidas instruções sobre como criar um código novo.

- **Exemplos concretos**: A inclusão de exemplos de saída (como o JSON com sentimento "POSITIVO" e a sugestão de funcionalidade "EDITAR_PERFIL") é uma excelente prática, pois ajuda o modelo a entender melhor a estrutura esperada e fornece contexto adicional, o que pode aumentar a precisão da resposta.

- **Formato de saída e foco no resultado final**: O uso de um formato JSON definido ajuda a garantir que a resposta gerada esteja em um formato utilizável e válido. Além disso, a instrução final que diz "A resposta deve ser um JSON válido, sem nenhum texto adicional além do JSON" é uma boa prática para garantir que a resposta seja apenas o que foi solicitado, sem informações irrelevantes.

>**Dividir o Prompt em Etapas**:
Em casos onde o prompt é muito longo ou complexo, deve-se considerar dividi-lo em etapas menores. Por exemplo, primeiro pedir para identificar o sentimento, depois identificar as funcionalidades sugeridas, e finalmente gerar a resposta personalizada. Foram feitos testes utilizando prompts mais simples e em etapas menores, entretanto, no caso desta funcionalidade, não houveram melhorias significativas, e como a estrutura da funcionalidade está adequada para uma única etapa, optou-se por manter a simplicidade e realizar somente uma chamada a API da OpenAI para realizar a análise do feedback.

- **Para a tarefa de classificação de SPAM, foi elaborado o seguinte prompt**:

```
private String getIsSpamContent(String feedback) {
    return """
            Determine se o seguinte feedback é um SPAM ou NAO_SPAM.
            Um feedback pode ser considerado SPAM caso ele tenha:
            - Conteúdo agressivo;
            - Linguagem promocional;
            - Links irrelevantes ou externos;
            - Conteúdo que não faça sentido em relação à startup Alumind;
            - Dentre outros motivos que caracterizam SPAM.

            Aqui está uma breve descrição da Alumind, para ajudar você a determinar se o feedback faz ou não sentido:
            A Alumind é uma startup que oferece um aplicativo focado em bem-estar e saúde mental. Proporcionamos aos usuários acesso a meditações guiadas, sessões de terapia e conteúdos educativos sobre saúde mental.

            Exemplo:
            - Um feedback sobre a funcionalidade de meditações guiadas faz sentido;
            - Um feedback que promova produtos de beleza ou inclua links irrelevantes não faz sentido.

            Retorne somente "SPAM" ou "NAO_SPAM", sem absolutamente nenhum texto além dessas palavras.

            Feedback: "%s"
           """.formatted(feedback);
}
```

As práticas utilizadas no prompt acima são detalhadas a seguir:

- **Instruções Claras e Simples**: O prompt apresenta de forma direta o que o modelo deve fazer: determinar se o feedback é "SPAM" ou "NAO_SPAM". Instruções claras ajudam a minimizar a ambiguidade e tornam o comportamento do modelo mais previsível.

- **Definição de Critérios**: Ao listar os critérios para determinar se o feedback é SPAM (conteúdo agressivo, linguagem promocional, links irrelevantes, etc.), o prompt estabelece limites claros para a classificação, o que ajuda o modelo a tomar decisões mais consistentes.

- **Contextualização**: A inclusão de uma breve descrição da Alumind fornece contexto ao modelo, o que é essencial para distinguir entre feedback relevante e irrelevante, especialmente no que diz respeito à natureza da empresa (bem-estar e saúde mental).

- **Exemplo Ilustrativo**: A utilização de exemplos concretos (feedback sobre meditações guiadas versus promoção de produtos de beleza) oferece uma diretriz prática para o modelo, facilitando a aplicação dos critérios definidos.
  
- **Formato de Saída Definido**: A instrução para retornar somente as strings "SPAM" ou "NAO_SPAM", sem texto adicional, é uma boa prática para garantir que o resultado seja formatado corretamente e sem informações irrelevantes. Isso evita ruído na resposta, o que é especialmente útil em cenários automatizados.

#### Escolha do Modelo de LLM da OpenAI (GPT-4)

A escolha do melhor modelo de LLM da OpenAI depende de alguns fatores como desempenho desejado, complexidade das tarefas e custos. Para o cenário proposto, os seguintes pontos devem ser levados em consideração:

- **Entrada estruturada**: O modelo irá receber prompts claros e bem definidos, com feedbacks e códigos de funcionalidades específicos.
- **Geração de JSON**: O modelo deve realizar processamento de linguagem natural para interpretar sentimentos e funcionalidades, gerando respostas estruturadas (JSON).
- **Necessidade de alta precisão**: Como o modelo deve identificar corretamente sentimentos e sugerir códigos apropriados, a precisão dele é muito importante.
- **Uso de modelo de LLM em produção**: O cenário envolve um ambiente de produção, então a confiabilidade e consistência das respostas são fundamentais.

Levando os pontos acima em consideração, foi escolhido o **GPT-4** como modelo de LLM, pelas considerações abaixo:

- **Desempenho**: O GPT-4 é um dos modelos mais avançados da OpenAI, com uma compreensão profunda de texto e uma capacidade superior de lidar com nuances, raciocínio e tarefas mais complexas. Ele é excelente em capturar sentimentos e identificar funcionalidades sugeridas de maneira mais precisa e detalhada.

- **Custo**: Apesar deste ser um dos modelos mais caros da OpenAI, para o escopo deste projeto, o custo não é um fator limitante e a prioridade é a qualidade máxima.

- **Precisão e Consistência**: O GPT-4 é particularmente bom em cenários onde o contexto do feedback é complexo ou onde o reconhecimento de padrões sutis é importante.

Considerando que a precisão é a prioridade absoluta e o orçamento não é um grande problema, o **GPT-4** é uma escolha sólida. Ele é capaz de compreender corretamente os sentimentos e as sugestões de funcionalidades, mesmo em casos mais complexos.

>**Obs.:** Também foram testados os modelos **GPT-3.5 Turbo** e o modelo **GPT-4o Mini**. Ambos apresentaram falhas e inconsistências em alguns feedbacks mais ambíguos, enquanto o modelo **GPT-4** foi bem sucedido nestes feedbacks.

#### Possibilidade de Implementação Utilizando Paradigma Reativo

Optou-se por implementar a solução usando um modelo de programação convencional, visando simplicidade de implementação e na avaliação ao invés de alto desempenho e escalabilidade elevada. Entretanto, a solução também poderia ser fornecida utilizando programação reativa, através do módulo WebFlux da Framework Spring, que possibilita o processamento assíncrono de requisições utilizando processos não-bloqueantes. Esta abordagem é naturalmente implementada em conjunto com bancos NoSQL (como o MongoDB, que possui um driver reativo nativo). Porém, apesar do MySQL não possuir um suporte nativo a I/O reativo diretamente, é possível utilizar o R2DBC (um projeto que visa fornecer conectividade reativa para bancos de dados relacionais). Além disso, também é viável utilizar orientação a mensagens e eventos (através de mensageria, com Apache Kafka ou RabbitMQ).

#### Escolha na Utilização de Requisições REST ao Invés de SDKs

Por mais que no ambiente Spring existam SDKs para consumir os serviços oferecidos pela OpenAI, optou-se por manter a simplicidade e flexibilidade ao utilizar requisições REST para consumir a API da OpenAI.

#### Possiblidade de Uso do Lombok

Optou-se por não utilizar o Lombok na implementação do projeto. Por mais que eu esteja familiarizado e habituado a utilizar o Lombok, eu não possuo conhecimento se os avaliadores teriam o Lombok configurado em suas IDEs.

### Dúvidas sobre os requisitos

Eu tive dúvidas sobre o código que identifica unicamente cada funcionalidade. Interpretei que este código é uma tipo de grupo em que a funcionalidade sugerida é classificada, desta forma, o LLM poderia realizar uma categorização. Cheguei a considerar que o LLM deveria decidir se um determinado feedback não é relevante porque outro semelhante já havia sido recebido, porém, ele iria descartar feedbacks que sugerem funcionalidades semelhantes, mas por motivos diferentes, e a perda destas nuances não seria adequada. Além disso, quanto mais feedbacks processados, mais dados o LLM teria que memorizar, o que poderia causar problemas de desempenhos. Logo, interpretei que vão ter feedbacks com sugestões semelhantes, mas nestes casos, todos estarão dentro do mesmo grupo (por exemplo, "EDITAR_PERFIL"). Assim, em uma futura funcionalidade, seria possível recuperar uma grande quantidade de feedbacks de um determinado código e solicitar ao LLM que interprete e elenque as funcionalidades solicitadas.

## Melhorias Futuras

Pensei em duas possível funcionalidades utilizando LLMs. A primeira (Sistema de Relatórios Automáticos e Personalizados para o Time de Produtos) pode ser integrada diretamente na API de feedbacks, enquanto a segunda (Assistente de Saúde Mental Personalizado) seria integrada em outro serviço na plataforma Alumind.

### **(1) Sistema de Relatórios Automáticos e Personalizados para o Time de Produtos**

A proposta é criar um **Sistema de Geração Automática de Relatórios Personalizados** que utiliza LLMs para analisar os feedbacks coletados e identificar as funcionalidades mais solicitadas, classificando-as de acordo com os códigos previamente definidos. Esses relatórios seriam gerados periodicamente (diários, semanais ou mensais) e enviados automaticamente ao time de produtos para auxiliar no planejamento de futuras atualizações e decisões estratégicas.

O sistema agregaria dados como:

- **Funcionalidades mais solicitadas**: Quantidade de vezes que uma funcionalidade foi sugerida e suas variações.
- **Feedbacks Positivos/Negativos**: Análise de sentimento agregada para cada funcionalidade.
- **Tendências emergentes**: Sugestões de novas funcionalidades ou melhorias emergentes identificadas automaticamente pelo LLM a partir dos padrões de feedbacks.
- **Análise detalhada por funcionalidade**: Para cada funcionalidade (categorizada pelos códigos), o sistema poderia sugerir melhorias específicas com base nas nuances dos feedbacks coletados.

A categorização das funcionalidades com códigos únicos traz várias vantagens para a implementação dessa nova funcionalidade:

- **Facilidade de Agrupamento e Análise**: Como todas as sugestões de funcionalidades possuem um código único (como `"EDITAR_PERFIL"`), fica fácil agrupar feedbacks semelhantes e criar relatórios claros que mostram a frequência com que cada funcionalidade é solicitada, independentemente de variações na redação do feedback.

- **Padronização de Relatórios**: Ao utilizar códigos únicos para funcionalidades, o relatório pode apresentar dados de forma padronizada e consistente, o que facilita o entendimento do time de produtos sobre quais são as funcionalidades prioritárias.

- **Detecção de Tendências**: O LLM pode identificar novos padrões de feedback que não correspondem diretamente a um código existente. Nesse caso, o sistema pode gerar insights sobre funcionalidades emergentes que ainda não foram formalmente categorizadas no sistema.

- **Foco em Funcionalidades mais Relevantes**: A categorização permite priorizar automaticamente as funcionalidades que estão sendo mais solicitadas, sem que o time precise revisar manualmente cada feedback.
    

#### Exemplo de Relatório Gerado

- **Data**: Relatório semanal - 10/09/2024 a 17/09/2024
- **Feedbacks Totais Analisados**: 350
- **Sentimento Geral**: 75% positivo, 20% negativo, 5% inconclusivo.

#### Funcionalidades Solicitadas
| Código           | Funcionalidade     | Solicitações Totais | Sentimento Positivo (%) | Sentimento Negativo (%) | Melhoria Sugerida                                 |
|------------------|--------------------|---------------------|-------------------------|-------------------------|--------------------------------------------------|
| EDITAR_PERFIL     | Edição de Perfil   | 120                 | 90%                     | 10%                     | "Adicionar opções para editar foto de perfil."    |
| MODO_NOTURNO      | Modo Noturno       | 85                  | 88%                     | 12%                     | "Facilitar ativação automática com base no horário." |
| NOTIFICACOES_PERSONALIZADAS | Notificações Personalizadas | 50                  | 70%                     | 30%                     | "Permitir silenciar notificações específicas."   |


#### Tendências Emergentes Detectadas

- **Nova Funcionalidade Sugerida**: **Integração com Apple Health** – Vários feedbacks mencionam a necessidade de sincronizar dados com o aplicativo de saúde da Apple. Essa funcionalidade ainda não possui código, mas é uma tendência crescente.
- **Sugerido por 15% dos usuários que também mencionaram problemas com a edição de perfil.**

#### Como Funciona

1.  **Coleta e Classificação dos Feedbacks**:
    
    - A API recebe feedbacks dos usuários e classifica cada feedback usando os códigos únicos de funcionalidades sugeridas. Esses códigos já estão associados a funcionalidades previamente definidas, como `"EDITAR_PERFIL"` ou `"MODO_NOTURNO"`.
2.  **Análise de Sentimento**:
    
    - Usando um modelo LLM, os feedbacks são analisados quanto ao sentimento (POSITIVO, NEGATIVO, INCONCLUSIVO), o que permite gerar relatórios que mostram a percepção dos usuários sobre cada funcionalidade.
3.  **Geração de Relatórios Personalizados**:
    
    - O sistema utiliza LLMs para interpretar os dados coletados e gerar relatórios que são enviados periodicamente ao time de produtos. Além disso, o LLM pode gerar resumos automáticos e destacar funcionalidades emergentes ou problemas não identificados previamente.
4.  **Relatórios Automatizados**:
    
    - Relatórios são enviados automaticamente ao time de produtos via email ou integrados a ferramentas de colaboração como Slack ou Jira, com insights e sugestões para o próximo ciclo de desenvolvimento.

#### Implementação Técnica

- **O LLM seria responsável por**:

    - **Gerar insights** sobre tendências emergentes de funcionalidades.
    - **Criar resumos** de grandes volumes de feedbacks.
    - **Detectar** funcionalidades ou problemas que ainda não foram categorizados por um código de funcionalidade.

- **Pipeline de Geração de Relatórios**:

    - **Coleta de Dados**: Usar o mesmo banco de dados de feedbacks categorizados por código de funcionalidades.
    - **Análise de Sentimento e Frequência**: Agregar dados sobre o sentimento dos feedbacks e quantas vezes cada funcionalidade foi sugerida.
    - **Detecção de Padrões Emergentes**: Usar o LLM para analisar padrões nos feedbacks que ainda não estão formalmente categorizados.
    - **Criação de Relatório**: O LLM gera relatórios em formato legível e envia automaticamente para o time de produtos.

#### Alguns Benefícios em Potencial desta Funcionalidade:

- **Apoio à Decisão**: Oferece ao time de produtos dados valiosos e organizados sobre as funcionalidades mais demandadas, permitindo melhor priorização e decisões baseadas em dados.
- **Automatização**: O processo de geração de relatórios é automático, liberando o time de produtos de análises manuais demoradas.
- **Insights Avançados**: O uso de LLMs permite detectar tendências emergentes que poderiam passar despercebidas com uma análise manual.
- **Eficiência**: A categorização com códigos permite uma análise rápida e eficiente das funcionalidades mais solicitadas.

#### Possíveis Expansões:

- **Dashboards Interativos**: Integrar o sistema de relatórios com uma interface gráfica onde o time de produtos pode visualizar em tempo real as estatísticas e tendências.
- **Feedback em Tempo Real**: Criar alertas em tempo real quando uma nova tendência ou problema emergente for detectado pelo LLM.

### (2) Assistente de Saúde Mental Personalizado

A proposta é criar um **Assistente de Saúde Mental Personalizado**, baseado em LLMs, que interage diretamente com os usuários para fornecer suporte emocional, orientações e sugestões de práticas de bem-estar mental, de forma dinâmica e personalizada.

Esse assistente seria integrado ao aplicativo Alumind e poderia conversar com os usuários de maneira natural, realizando algumas funções como:

- **Check-ins emocionais diários**: Perguntando como o usuário está se sentindo e sugerindo meditações guiadas, técnicas de respiração, ou mesmo sessões de terapia personalizadas.
- **Sugestões de conteúdo**: Com base no feedback emocional do usuário, o assistente poderia sugerir vídeos, artigos ou podcasts disponíveis no Alumind, adaptados às necessidades emocionais do momento.
- **Plano de Acompanhamento**: O assistente pode ajudar a montar e monitorar um plano de bem-estar mental, sugerindo atividades diárias e lembretes personalizados.

#### Exemplos de Interações

- **Check-in diário**:
    - Assistente: "Olá, como você está se sentindo hoje?"
    - Usuário: "Estou um pouco ansioso com o trabalho."
    - Assistente: "Sinto muito por isso. Talvez uma meditação de 5 minutos possa ajudar a aliviar a tensão. Posso sugerir uma?"
- **Sugestão de conteúdo**:
    - Usuário: "Tenho sentido muita dificuldade para dormir."
    - Assistente: "Entendo, isso pode ser muito desconfortável. Que tal escutar um áudio sobre higiene do sono? Ou eu posso te guiar em uma meditação relaxante."

#### Essa funcionalidade seria útil para o Alumind pelos seguintes motivos:

- O assistente permitiria um nível de personalização profundo, criando uma experiência única para cada usuário. O sistema poderia adaptar-se ao humor e às necessidades específicas de cada pessoa, aumentando a retenção de usuários e o impacto positivo sobre sua saúde mental.

- Além disso, o assistente manteria os usuários engajados ao longo do tempo, sugerindo continuamente novos conteúdos e práticas. Isso seria benéfico tanto para os usuários (que receberiam suporte contínuo) quanto para a plataforma (que teria um aumento na frequência de uso).

- Muitas pessoas que enfrentam dificuldades em horários fora do expediente convencional também seriam beneficiadas. O assistente, disponível 24/7, permitiria que os usuários encontrassem ajuda imediata sempre que necessário, melhorando a satisfação geral com a plataforma.

#### Essa funcionalidade poderia ser implementada da seguinte forma:

- **Integração com Modelos LLM**: Modelos de LLM (como GPT-4) seriam responsáveis pela criação de diálogos naturais e pela sugestão de atividades de bem-estar mental. O modelo precisaria ser treinado para entender o estado emocional dos usuários a partir de suas respostas e então oferecer suporte adequado.

- **Análise de Sentimento em Tempo Real**: Cada interação do usuário com o assistente seria analisada em tempo real para detectar sentimentos subjacentes (como estresse, tristeza ou alegria) e adaptar a resposta do assistente, utilizando a mesma abordagem de análise de sentimentos que já está em uso para feedbacks.

- **Sistema de Recomendação**: O assistente teria acesso a um banco de dados de conteúdos do Alumind (meditações, vídeos, podcasts, etc.) e seria capaz de recomendar conteúdos com base no feedback emocional do usuário. Para isso, seria necessário um sistema de classificação que conectasse os conteúdos às emoções mais apropriadas.

- **Monitoramento de Progressos**: A interação contínua com o assistente poderia gerar um histórico de sentimentos e ações realizadas pelo usuário. Esse histórico poderia ser visualizado pelo usuário em forma de gráficos ou insights, ajudando-o a entender seu progresso ao longo do tempo.
