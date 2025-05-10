package com.bookstore.book_store.Ollama3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class OllamaClient {
    
    private final WebClient webClient;
    
    @Value("${api.model.url}")
    private String apiUrl;

    @Value("{api.model.key}")
    private String apiKey;

    public OllamaClient(){
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public String callModel(String prompt) {
        String requestBody = String.format("{\"model\": \"deepseek/deepseek-v3-base:free\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}",
                prompt);

        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> response.path("choices").get(0).path("message").path("content").asText())
                .block();
    }
}