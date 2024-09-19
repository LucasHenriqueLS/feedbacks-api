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

	public Feedback analyzeFeedback(String feedback, Set<String> functionalityCodes) throws Exception {
        return objectMapper.readValue(requestLLM(getAnalyzeFeedbackContent(feedback, functionalityCodes)), Feedback.class);
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
		return new LLMRequest("gpt-3.5-turbo", List.of(new Message("user", content)));
	}

	private String getIsSpamContent(String feedback) {
		return """
				Determine se o seguinte feedback é um SPAM ou não. Um feedback pode ser considerado um é SPAM caso ele tenha conteúdo agressivo ou seu conteúdo não tem sentido em relação a startup Alumind.
				A seguir, uma preve descrição da plataforma Alumind, para que você possa determinar se o feedback faz ou não sentido com a plataforma:
				A Alumind é uma startup que oferece um aplicativo focado em bem-estar e saúde mental, proporcionando aos usuários acesso a meditações guiadas, sessões de terapia, e conteúdos educativos sobre saúde mental.
				Você deve retornar somente as strings "SPAM" e "NAO_SPAM", sem absolutamente nenhum texto além de alguma destas strings.

				Feedback: "%s"
			   """.formatted(feedback);
	}

	private String getAnalyzeFeedbackContent(String feedback, Set<String> functionalityCodes) {
		return """
				Através deste feedback:
				"%s"

				Monte o seguinte Json:
				{
					"sentiment": (( Determine se o sentimento do usuário no feedback é "POSITIVO", "NEGATIVO" ou "INCONCLUSIVO" e atribua o valores apropriado no campo sentiment )),
					"requestedFeatures": (( O feedback pode conter uma ou mais funcionalidades sugeridas. Identifique-as, caso haja, e forneça um código (escolha entre as opções em uma lista fornecida, caso não haja códigos ou nenhum dos códigos fornecidos seja apropriado, elabore um novo código seguindo os padrões) e uma descrição do porquê a funcionalidade é importante ))
						[
							{
								"code": (( Por exemplo: "MELHORAR_DESEMPENHO", a lista de códigos disponíveis é a seguinte (reforçando, caso não haja códigos ou nenhum dos fornecidos seja apropriado, elabore um novo código) %s )),
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
				
				Retorne somente o Json, sem absolutamente nenhum texto além do Json.
			   """.formatted(feedback, functionalityCodes.toString());
	}
}
