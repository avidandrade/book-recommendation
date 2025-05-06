package com.bookstore.book_store.Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoogleService {
    private final int maxResults = 5;
    private final String APL_Url = "https://www.googleapis.com/books/v1/volumes?q=";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    
    
    public List<Book> fetchRecommendedBooks (List<String> bookTitles, String userId){
        if(bookTitles == null || bookTitles.isEmpty()){
            throw new RuntimeException("Ollama failed to generate book titles!");
        }
        List<Book> books = new ArrayList<>();
        for(String title : bookTitles){
            String url = APL_Url + title + "&maxResults=" + maxResults;
            String response = restTemplate.getForObject(url,String.class);
            books.add(parseResponse(response,userId));
        }

        return books;
    }

    public Book fetchBookData(String isbn, String userId){
        String url = APL_Url + "isbn:" + isbn;
        String response = restTemplate.getForObject(url, String.class);
        
        return parseResponse(response, userId);
    }
    public Book parseResponse(String response, String userId){
        try{
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("items");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String title = item.path("volumeInfo").path("title").asText();
                    String authors = item.path("volumeInfo").path("authors").toString();
                    String description = item.path("volumeInfo").path("description").asText();
                    int rating = item.path("volumeInfo").path("averageRating").asInt();
                    
                    // Extract ISBN
                    String isbn = "";
                    JsonNode industryIdentifiers = item.path("volumeInfo").path("industryIdentifiers");
                    if (industryIdentifiers.isArray()) {
                        for (JsonNode identifier : industryIdentifiers) {
                            if ("ISBN_13".equals(identifier.path("type").asText())) {
                                isbn = identifier.path("identifier").asText();
                                break;
                            }
                        }
                    }

                    // Extract genre (categories)
                    String genre = "";
                    JsonNode categories = item.path("volumeInfo").path("categories");
                    if (categories.isArray() && categories.size() > 0) {
                        genre = categories.get(0).asText();
                    }
                    String coverImageUrl = "";
                    JsonNode imageLinks = item.path("volumeInfo").path("imageLinks");
                    if (imageLinks.has("thumbnail")) {
                        coverImageUrl = imageLinks.path("thumbnail").asText();
                    }
                    coverImageUrl = coverImageUrl.replace("http://", "https://");

                    return new Book(title, authors, genre,description, isbn, coverImageUrl, rating,userId);
                }       
            }
        }catch(IOException e){
            return null;
        }
        return null;
    }


}
