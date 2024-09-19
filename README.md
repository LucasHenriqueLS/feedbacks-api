
# Alumind - Microsserviço de Análise de Feedbacks

## Descrição do Projeto

Este projeto é um protótipo de uma aplicação web para a **Alumind**, uma startup focada em bem-estar e saúde mental. A aplicação tem como objetivo analisar os feedbacks dos usuários, classificá-los de acordo com seu sentimento, sugerir possíveis funcionalidades a partir desses feedbacks e gerar uma resposta automática personalizada para cada feedback. Além disso, a aplicação possui detecção de SPAM.

## Tecnologias Utilizadas

- **Java 21**: Linguagem principal do projeto.

- **Spring Boot**: Framework utilizado para simplificar o desenvolvimento da aplicação.

- **Spring Data JPA**: Utilizado para persistência de dados no banco de dados.

- **MySQL**: Banco de dados relacional utilizado.

- **LLM (Chat-GPT)**: Utilizado para análise de sentimentos, processamento do feedback, geração de respostas automáticas e personalizadas e detecção de SPAM nos feedbacks.

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

## Como Executar o Projeto

### Pré-requisitos

1. **Java 21+**: Certifique-se de ter o Java 21 ou mais recente instalado.

2. **Banco de Dados**: É necessário ter acesso a um servidor MySQL para armazenar as entidades.

3. **OpenAI API Key**: Você precisará de uma chave de API da OpenAI para a análise dos feedbacks e detecção de SPAM.

### Passos para Configuração

1. **Clone o Repositório**:

`https://github.com/LucasHenriqueLS/feedbacks-api.git`

2. **Configuração do Banco de Dados e da API da OpenAI**: Atualize o arquivo `application.yml` com suas credenciais de banco de dados e sua chave de API da OpenAI (substitua `${ ... }` pelo respectivo valor ou adicione `: valor_do_campo` após o nome do campo, da mesma forma que na configuração da porta):
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
- O projeto foi implementado de forma que o banco e as tabelas fossem gerados automaticamente, com o intuito de simplificar a execução e a avaliação do projeto.

4. **Executar a Aplicação**: Com o **Maven** ou **Gradle**, execute a aplicação localmente:

`./mvnw spring-boot:run`

- É recomendado executar a aplicação em uma IDE como o Eclipse (Spring Tools Suite) ou IntelliJ IDEA.

5. **Acessar a API**: A aplicação estará disponível em `http://localhost:10000`.

## Detalhes de Implementação

### Decisões Técnicas

#### Geração de Respostas Personalizadas

Optou-se por integrar a geração de respostas personalizadas diretamente no endpoint `/feedbacks`, porque:

- Durante o processamento do feedback, já temos todas as informações necessárias para gerar uma resposta personalizada (sentimento e funcionalidades sugeridas). Assim, evitamos duplicidade de chamadas à API e mantemos o fluxo de processamento eficiente. Além disso, cada feedback que não for considerado um SPAM deve receber uma resposta, o que justifica realizar todo o processamento em uma única chamada à API.

- Em vez de criar um novo endpoint dedicado apenas à geração de respostas, centralizamos todo o processamento em uma única chamada, evitando criar camadas adicionais de lógica, o que reduz a sobrecarga no sistema, simplifica a manutenção do código e o torna mais simples de entender e testar.

#### Possibilidade de Implementação Utilizando Paradigma Reativo

Optou-se por implementar a solução usando um modelo de programação convencional, visando simplicidade de implementação e na avaliação ao invés de alto desempenho e escalabilidade elevada. Entretanto, a solução também poderia ser fornecida utilizando programação reativa, através do módulo WebFlux da Framework Spring, que possibilita o processamento assíncrono de requisições utilizando processos não-bloqueantes. Esta abordagem é naturalmente implementada em conjunto com bancos NoSQL (como o MongoDB, que possui um driver reativo nativo), porém, apesar do MySQL não possuir um suporte nativo a I/O reativo diretamente, é possível utilizar o R2DBC (um projeto que visa fornecer conectividade reativa para bancos de dados relacionais). Além disso, também é viável utilizar orientação a mensagens e eventos (através de mensageria, com Apache Kafka ou RabbitMQ).

#### Possiblidade de Uso do Lombok

Optou-se por não utilizar o Lombok na implementação da solução. Por mais que eu esteja familiarizado e habituado a utilizar o Lombok, eu não possuía conhecimento se os avaliadores teriam o Lombok configurado em suas IDEs.

### Dúvidas sobre os requisitos

Eu tive dúvidas o código que identifica unicamente cada funcionalidade. Interpretei que este código é uma tipo  de grupo em que a funcionalidade sugerida é classificada, desta forma, o LLM realiza uma categorização. Cheguei a considerar que o LLM deveria decidir que um determinado feedback não é relevante porque outro semelhante já havia sido recebido, porém, ele iria descartar feedbacks que sugerem funcionalidades semelhantes, mas por motivos diferentes, e a perda destas nuancias não seria adequada. Além disso, quanto mais feedbacks, mais dados ele terá que memorizar, o que poderia causar problemas de desempenhos. Logo, interpretei que vão ter feedbacks com sugestões semelhantes, mas nestes casos, todos estarão dentro do mesmo grupo (por exemplo, "EDITAR_PERFIL"). Assim, em uma futura funcionalidade, seria possível recuperar uma grande quantidade de  feedbacks de um determinado grupo e solicitar ao LLM que ele interprete e elenque as funcionalidades solicitadas.

## Melhorias Futuras

### Assistente de Saúde Mental Personalizado

A proposta é criar um **Assistente de Saúde Mental Personalizado**, baseado em LLMs, que interage diretamente com os usuários para fornecer suporte emocional, orientações e sugestões de práticas de bem-estar mental, de forma dinâmica e personalizada.

Esse assistente seria integrado ao aplicativo Alumind e poderia conversar com os usuários de maneira natural, realizando algumas funções como:

- **Check-ins emocionais diários**: Perguntando como o usuário está se sentindo e sugerindo meditações guiadas, técnicas de respiração, ou mesmo sessões de terapia personalizadas.
- **Sugestões de conteúdo**: Com base no feedback emocional do usuário, o assistente poderia sugerir vídeos, artigos ou podcasts disponíveis no Alumind, adaptados às necessidades emocionais do momento.
- **Plano de Acompanhamento**: O assistente pode ajudar a montar e monitorar um plano de bem-estar mental, sugerindo atividades diárias e lembretes personalizados.

#### Exemplos de Interações

- **Check-in diário**:
    -   Assistente: "Olá, como você está se sentindo hoje?"
    -   Usuário: "Estou um pouco ansioso com o trabalho."
    -   Assistente: "Sinto muito por isso. Talvez uma meditação de 5 minutos possa ajudar a aliviar a tensão. Posso sugerir uma?"
- **Sugestão de conteúdo**:
    -   Usuário: "Tenho sentido muita dificuldade para dormir."
    -   Assistente: "Entendo, isso pode ser muito desconfortável. Que tal escutar um áudio sobre higiene do sono? Ou eu posso te guiar em uma meditação relaxante."

#### Essa funcionalidade seria útil para o Alumind pelos seguintes motivos:

- O assistente permitiria um nível de personalização profundo, criando uma experiência única para cada usuário. O sistema poderia adaptar-se ao humor e às necessidades específicas de cada pessoa, aumentando a retenção de usuários e o impacto positivo sobre sua saúde mental.

- Além disso, o assistente manteria os usuários engajados ao longo do tempo, sugerindo continuamente novos conteúdos e práticas. Isso seria benéfico tanto para os usuários (que receberiam suporte contínuo) quanto para a plataforma (que teria um aumento na frequência de uso).

- Muitas pessoas que enfrentam dificuldades em horários fora do expediente convencional também seriam beneficiadas. O assistente, disponível 24/7, permitiria que os usuários encontrassem ajuda imediata sempre que necessário, melhorando a satisfação geral com a plataforma.

#### Essa funcionalidade poderia ser implementada da seguinte forma:

- **Integração com Modelos LLM**: Modelos de LLM (como GPT-4) seriam responsáveis pela criação de diálogos naturais e pela sugestão de atividades de bem-estar mental. O modelo precisaria ser treinado para entender o estado emocional dos usuários a partir de suas respostas e então oferecer suporte adequado.

- **Análise de Sentimento em Tempo Real**: Cada interação do usuário com o assistente seria analisada em tempo real para detectar sentimentos subjacentes (como estresse, tristeza ou alegria) e adaptar a resposta do assistente, utilizando a mesma abordagem de análise de sentimentos que já está em uso para feedbacks.

- **Sistema de Recomendação**: O assistente teria acesso a um banco de dados de conteúdos do Alumind (meditações, vídeos, podcasts, etc.) e seria capaz de recomendar conteúdos com base no feedback emocional do usuário. Para isso, seria necessário um sistema de classificação que conectasse os conteúdos às emoções mais apropriadas.

- **Monitoramento de Progressos**: A interação contínua com o assistente poderia gerar um histórico de sentimentos e ações realizadas pelo usuário. Esse histórico poderia ser visualizado pelo usuário em forma de gráficos ou insights, ajudando-o a entender seu progresso ao longo do tempo.
