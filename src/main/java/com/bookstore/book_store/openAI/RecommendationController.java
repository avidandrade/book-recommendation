package com.bookstore.book_store.openAI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private openAIService openAIService;

    @GetMapping
    public String getRecommendations(@RequestParam String preferences) {
        return openAIService.getRecommendations(preferences);
    }
}
