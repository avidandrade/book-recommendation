package com.bookstore.book_store.Ollama3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OllamaService {

    private final WebClient webClient;
    
    public OllamaService(@Value("${ollama.api.url}") String ollamaApiUrl) {
        this.webClient = WebClient.builder().baseUrl(ollamaApiUrl).build();
    }

    public List<String> getRecommendedBookTitle(String userInput) {

        String prompt = "Recommend five bestselling book titles that strongly reflect the emotion: '"
        + userInput + "'. Respond with a comma-separated list, with no numbering or extra text. Example: 'Book1, Book2, Book3, Book4, Book5'.";


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
        if(response != null){
            return Arrays.asList(response.split("\\s*,\\s*")); 
        }else{
            return new ArrayList<>();
        }
    }

    public String getBookSummary(@RequestParam String title){
        String prompt = "Provide a brief summary of the book titled: '" + title + "'.";

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

    public String getBookReview(@RequestParam String title, @RequestParam int rating){
        String prompt = "Provide a brief review of the book titled: '" + title + " and explain why it was rated " + rating + " stars.";

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

    

    // Inner class to match JSON structure
    private static class OllamaResponse {
        private String response;

        public String getResponse() {
            return response;
        }
    }
}