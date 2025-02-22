package com.bookstore.book_store.Ollama3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationController {

    @Autowired
    private OllamaService ollamaService;

    @GetMapping("/generate")
    public List<String> generateQuery(@RequestParam String emotion){
        return ollamaService.getRecommendedBookTitle(emotion);
    }

    @GetMapping("/summary")
    public String getSummary(@RequestParam String title){
        return ollamaService.getBookSummary(title);
    }

    @GetMapping("/review")
    public String getReview(@RequestParam String title, @RequestParam int rating){
        return ollamaService.getBookReview(title, rating);
    }
}
