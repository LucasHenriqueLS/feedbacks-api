package br.com.alura.alumind.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.alumind.dto.request.LLMRequest;
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
        var uri = "https://api.openai.com/v1/chat/completions";
		var headers = new HttpHeaders();
		headers.set("Authorization", String.format("Bearer %s", openaiApiKey));
		headers.set("Content-Type", "application/json");
		
		var body = new LLMRequest("gpt-3.5-turbo", getIsSpamContent(
				"Gosto muito de usar o Alumind! Está me ajudando bastante em relação a alguns problemas que tenho. Só queria que houvesse uma forma mais fácil de eu mesmo realizar a edição do meu perfil dentro da minha conta"
				));
		
		var request = new HttpEntity<>(body, headers);
		var result = restTemplate.exchange(uri, HttpMethod.POST, request, LLMResponse.class);

        return "SPAM".equalsIgnoreCase(result.getBody().getContent());
    }

	public Feedback analyzeFeedback(String feedback) throws Exception {
		var uri = "https://api.openai.com/v1/chat/completions";
		var headers = new HttpHeaders();
		headers.set("Authorization", String.format("Bearer %s", openaiApiKey));
		headers.set("Content-Type", "application/json");
		
		var body = new LLMRequest("gpt-3.5-turbo", getAnalyzeFeedbackContent(
				"Gosto muito de usar o Alumind! Está me ajudando bastante em relação a alguns problemas que tenho. Só queria que houvesse uma forma mais fácil de eu mesmo realizar a edição do meu perfil dentro da minha conta"
				));
		
		var request = new HttpEntity<>(body, headers);
		var result = restTemplate.exchange(uri, HttpMethod.POST, request, LLMResponse.class);

        return objectMapper.readValue(result.getBody().getContent(), Feedback.class);
    }
	
	private String getIsSpamContent(String feedback) {
		return """
				Determine se o seguinte feedback é um SPAM ou não. Um feedback pode ser considerado um é SPAM caso ele tenha conteúdo agressivo ou seu conteúdo não tem sentido com a Alumind
				(A Alumind é uma startup que oferece um aplicativo focado em bem-estar e saúde mental, proporcionando aos usuários acesso a meditações guiadas, sessões de terapia, e conteúdos educativos sobre saúde mental).
				Você deve retornar somente as strings "SPAM" e "NAO_SPAM", sem absolutamente nenhum texto além de alguma destas strings.

				Feedback: "%s"
				""".formatted(feedback);
	}

	private String getAnalyzeFeedbackContent(String feedback) {
		return """
				Através deste feedback:
				"%s"

				Monte o seguinte Json:
				{
					"sentiment": (( Determine se o sentimento do usuário no feedback é "POSITIVO", "NEGATIVO" ou "INCONCLUSIVO" e atribua o valores apropriado no campo sentiment )),
					"requested_features": (( O feedback pode conter uma ou mais funcionalidades sugeridas. Identifique-as, caso haja, e forneça um código e uma descrição do porquê a funcionalidade é importante ))
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
				
				Retorne somente o Json, sem absolutamente nenhum texto além do Json.
				""".formatted(feedback);
	}
}
