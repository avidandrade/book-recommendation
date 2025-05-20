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

    public OllamaClient(@Value("${api.model.url}") String apiUrl, @Value("${api.model.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public String callModel(String prompt) {
        String requestBody = String.format("{\"inputs\": \"%s\"}", prompt);

        return webClient.post()
                .uri("/models/deepseek-ai/deepseek-v3-base")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(JsonNode::toPrettyString)
                .block();
    }
}