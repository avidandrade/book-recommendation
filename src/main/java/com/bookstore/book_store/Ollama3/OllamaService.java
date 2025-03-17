package com.bookstore.book_store.Ollama3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {

    @Autowired
    private OllamaClient ollamaClient;
    
    public List<String> getRecommendedBookTitle(String userInput) {

        String prompt = "Recommend five bestselling book titles that strongly reflect the emotion: '"
        + userInput + "'. Respond with a comma-separated list, with no numbering or extra text. Example: 'Book1, Book2, Book3, Book4, Book5'.";

        String response = ollamaClient.callModel(prompt); // Block to wait for the full response
        if(response != null){
            return Arrays.asList(response.split("\\s*,\\s*")); 
        }else{
            return new ArrayList<>();
        }
    }

    public String getBookSummary(String title){
        String prompt = "Provide a brief and concise summary of the book titled: '" + title + "'.";

        return ollamaClient.callModel(prompt);
    }

    public String getBookReview(String title, int rating){
        String prompt = "Summarize the reviews of other reviewers regarding the book titled: '" + title + "'. Explain the reasons behind its " + rating + " star rating, highlighting key factors that influenced the overall rating.";

        return ollamaClient.callModel(prompt);
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