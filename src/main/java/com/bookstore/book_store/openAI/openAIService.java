package com.bookstore.book_store.openAI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class openAIService {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "YOUR_OPENAI_API_KEY";

    public String getRecommendations(String userPreferences) {
        RestTemplate restTemplate = new RestTemplate();

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + API_KEY);
        headers.add("Content-Type", "application/json");

        // Build the JSON request body
        String requestBody = """
        {
            "model": "gpt-4",
            "messages": [
                {"role": "system", "content": "You are an expert book recommendation system."},
                {"role": "user", "content": "Recommend books for me based on: " + userPreferences}
            ],
            "max_tokens": 300
        }
        """;

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send POST request to OpenAI API
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

        try {
            // Parse response to extract recommendations
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving recommendations.";
        }
    }
}