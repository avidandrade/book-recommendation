package com.bookstore.book_store.Ollama3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecommendationController {

    @Autowired
    private OllamaService ollamaService;

    @GetMapping("/generate")
    public String generateQuery(@RequestParam String emotion){
        return ollamaService.getRecommendedBookTitle(emotion);
    }
}
