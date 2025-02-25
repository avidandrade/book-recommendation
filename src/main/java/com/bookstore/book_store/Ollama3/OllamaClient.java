package com.bookstore.book_store.Ollama3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class OllamaClient {
    
    private WebClient webClient;
    
    public OllamaClient( @Value("${ollama.api.url}")String ollamaApiUrl) {
        this.webClient = WebClient.builder().baseUrl(ollamaApiUrl).build();
    }

    public String callModel(String prompt){
        String response = webClient.post()
                .uri("/api/generate")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue("{\"model\": \"llama3\", \"prompt\": \"" + prompt + "\"}")
                .retrieve()
                .bodyToFlux(OllamaResponse.class) // Deserialize streamed JSON objects
                .map(OllamaResponse::getResponse) // Extract only the response text
                .collectList() // Collect all parts into a list
                .map(list -> String.join("", list)) // Join parts into a single response
                .block(); // Block to wait for the full response

        return response;
    }

    private static class OllamaResponse {
        private String response;

        public String getResponse() {
            return response;
        }
    }
}
