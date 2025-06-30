package com.bookstore.book_store.Ollama3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OllamaClient {
    
    private final WebClient webClient;

    public OllamaClient(@Value("${api.model.url}") String apiUrl, @Value("${api.model.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public String callModel(String prompt) {
        String requestBody = String.format("{\"model\": \"meta-llama/llama-3.3-70b-instruct:free\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}", prompt);

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}