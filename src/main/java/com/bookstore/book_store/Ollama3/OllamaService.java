package com.bookstore.book_store.Ollama3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OllamaService {


    private final OllamaClient ollamaClient;

    public OllamaService(OllamaClient ollamaClient){
        this.ollamaClient = ollamaClient;
    }
    

    public List<String> getRecommendedBookTitle(String userInput){
        var prompt = "Recommend five bestselling book titles that strongly reflect the emotion: '"
        + userInput + "'. Respond with a comma-separated list, with no numbering or extra text. Example: 'Book1, Book2, Book3, Book4, Book5'. just the book titles";

        try{
            String jsonResponse = ollamaClient.callModel(prompt);
            JsonNode root = new ObjectMapper().readTree(jsonResponse);
            String response  = root.path("choices").get(0).path("message").path("content").asText();

            // Block to wait for the full response
             if(response != null){
                return Arrays.asList(response.split("\\s*,\\s*"));
             }else{
                System.out.println("Respones was empty");
             }
        }catch(Exception error){
            System.err.println("Failed to parse JSON response");
        }
        return new ArrayList<>();
    }

    
    public String getBookSummary(String title){
        var prompt = "Provide a brief and concise summary of the book titled: '" + title + "'.";
        String lolW = "didnt work";
        try{
            String jsonResponse = ollamaClient.callModel(prompt);
            JsonNode root = new ObjectMapper().readTree(jsonResponse);
            String response = root.path("choices").get(0).path("message").path("content").asText();

            return response;
        }catch(Exception e){
            System.out.println("Not Book Summary genearted!");
        }
        return lolW;
    }


    public String getBookReview(String title, int rating){
        String prompt = "Summarize the opinions of other reviewers regarding the book titled: '" + title + "'. Explain the reasons behind its " + rating + " star rating, highlighting key factors that influenced the overall rating in one short paragraph.";
         try{
            String jsonResponse = ollamaClient.callModel(prompt);
            JsonNode root = new ObjectMapper().readTree(jsonResponse);
            String response = root.path("choices").get(0).path("message").path("content").asText();

            return response;
        }catch(Exception e){
            System.out.println("Not Book Summary genearted!");
        }
        return null;
    }
    

    public List<String> getMoreBooks(String userInput, List<String> titles) {

        String prompt = "Recommend five bestselling book titles that strongly reflect the emotion: '"
        + userInput + "'. Do not include any of the following titles: " + String.join(", ", titles) 
        + ". Respond with a comma-separated list, with no numbering or extra text. Example: 'Book1, Book2, Book3, Book4, Book5'.";

        String response = ollamaClient.callModel(prompt);
        if(response != null){
            return Arrays.asList(response.split("\\s*,\\s*")); 
        }else{
            return new ArrayList<>();
        }
    }
}