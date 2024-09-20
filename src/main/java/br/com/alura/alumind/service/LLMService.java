package br.com.alura.alumind.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.alumind.dto.request.LLMRequest;
import br.com.alura.alumind.dto.request.Message;
import br.com.alura.alumind.dto.response.LLMResponse;
import br.com.alura.alumind.model.Feedback;

@Service
public class LLMService {
	
	@Value("${openai_api_key}")
	private String openaiApiKey;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public Boolean isSpam(String feedback) {
        return "SPAM".equalsIgnoreCase(requestLLM(getIsSpamContent(feedback)));
    }

	public Feedback analyzeFeedback(String feedback, Set<String> functionalityCodes, Integer attempts) throws Exception {
        try {
			return objectMapper.readValue(requestLLM(getAnalyzeFeedbackContent(feedback, functionalityCodes)), Feedback.class);
		} catch (Exception e) {
			// Se o LLM não retornar a resposta em um formato JSON válido, ou ocorrer algum outro erro com a API da OpenAI, tenta novamente, até um limite de "attempts" tentativas.
			e.printStackTrace();
        	if (attempts > 0) {
        		return analyzeFeedback(feedback, functionalityCodes, --attempts);	            		
        	}
        	throw e;
		}
    }

	private String requestLLM(String content) {
		var uri = getUrl();
		var headers = getHeaders();
		var body = getBody(content);

		var request = new HttpEntity<>(body, headers);
		var result = restTemplate.exchange(uri, HttpMethod.POST, request, LLMResponse.class);

		return result.getBody().getContent();
    }

	private String getUrl() {
		return "https://api.openai.com/v1/chat/completions";
	}

	private HttpHeaders getHeaders() {
		var headers = new HttpHeaders();
		headers.set("Authorization", String.format("Bearer %s", openaiApiKey));
		headers.set("Content-Type", "application/json");
		return headers;
	}

	private LLMRequest getBody(String content) {
		return new LLMRequest("gpt-4o", List.of(new Message("user", content)));
	}

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

	private String getAnalyzeFeedbackContent(String feedback, Set<String> functionalityCodes) {
	    return """
	            Através deste feedback:
	            "%s"

	            Monte o seguinte Json:
	            {
	                "sentiment": (( Determine se o sentimento do usuário no feedback é "POSITIVO" (Se o feedback conter elogios ou satisfação), "NEGATIVO" (Se o feedback incluir reclamações ou frustrações) ou "INCONCLUSIVO" (Se o sentimento não for claro ou for neutro) e atribua o valor apropriado no campo sentiment )),
	                "requestedFeatures": (( O feedback pode conter uma ou mais funcionalidades sugeridas. Identifique-as, caso haja, e forneça um código (escolha entre as opções em uma lista fornecida: %s. Caso nenhum dos códigos fornecidos seja apropriado, elabore um novo código seguindo os padrões, como "MELHORAR_DESEMPENHO". O código deve ser curto, descritivo e em letras maiúsculas) e uma descrição do porquê a funcionalidade é importante. Se o feedback não contiver nenhuma sugestão de funcionalidade ou se for muito vago, retorne uma lista vazia no JSON ))
	                    [
	                        {
	                            "code": (( Por exemplo: "MELHORAR_DESEMPENHO" )),
	                            "reason": (( Por exemplo: "O usuário gostaria de que a tela inicial carregasse mais rápido" ))
	                        }
	                    ]
	                "customResponse": (( Você deve gerar uma resposta personalizada para o feedback, criando uma mensagem a partir do sentimento identificado e das possíveis melhorias propostas no feedback. Por exemplo: "Obrigado pelo seu feedback positivo! Ficamos felizes em saber que você está gostando do Alumind. Vamos considerar sua sugestão de facilitar a edição do perfil para futuras atualizações." )) 
	            }

	            Um exemplo de Json é o seguinte:
	            {
	                "sentiment": "POSITIVO",
	                "requestedFeatures": [
	                    {
	                        "code": "EDITAR_PERFIL",
	                        "reason": "O usuário gostaria de realizar a edição do próprio perfil"
	                    }
	                ],
	                "customResponse": "Obrigado pelo seu feedback positivo! Ficamos felizes em saber que você está gostando do Alumind. Vamos considerar sua sugestão de facilitar a edição do perfil para futuras atualizações."
	            }
	            
	            A resposta deve ser um JSON válido, sem nenhum texto adicional além do JSON.
	           """.formatted(feedback, String.join(", ", functionalityCodes));
	}
}
