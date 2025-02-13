// package com.bookstore.book_store.openAI;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.ResponseEntity;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.node.ArrayNode;
// import com.fasterxml.jackson.databind.node.ObjectNode;

// @Service
// public class openAIService {

//     @Value("${openaiKey}")
//     private String openaiKey;

//     private static final String API_URL = "https://api.openai.com/v1/chat/completions";

//     public String getRecommendations(String userPreferences) {
//         RestTemplate restTemplate = new RestTemplate();

//         // Create request headers
//         HttpHeaders headers = new HttpHeaders();
//         headers.add("Authorization", "Bearer " + openaiKey);
//         headers.add("Content-Type", "application/json");

//         // Build the JSON request body using ObjectMapper
//         ObjectMapper mapper = new ObjectMapper();
//         ObjectNode requestBody = mapper.createObjectNode();
//         requestBody.put("model", "gpt-4");
//         requestBody.put("max_tokens", 300);

//         ArrayNode messages = mapper.createArrayNode();
//         ObjectNode systemMessage = mapper.createObjectNode();
//         systemMessage.put("role", "system");
//         systemMessage.put("content", "You are an expert book recommendation system.");
//         messages.add(systemMessage);

//         ObjectNode userMessage = mapper.createObjectNode();
//         userMessage.put("role", "user");
//         userMessage.put("content", "Recommend books for me based on: " + userPreferences);
//         messages.add(userMessage);

//         requestBody.set("messages", messages);

//         HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

//         try {
//             // Send POST request to OpenAI API
//             ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

//             // Parse response to extract recommendations
//             JsonNode root = mapper.readTree(response.getBody());
//             return root.get("choices").get(0).get("message").get("content").asText();
//         } catch (Exception e) {
//             e.printStackTrace();
//             return "Error retrieving recommendations.";
//         }
//     }
// }