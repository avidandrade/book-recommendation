package com.bookstore.book_store.s3;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class S3Controller {
    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }


    @PostMapping("/uploadCover")
    public String uploadBookCover(@RequestParam("imageUrl") String url, @RequestParam("title") String title) {  
        try{
            return s3Service.uploadImageToS3(url,title);
        }catch(Exception e){
            e.printStackTrace();
            return "Error uploading file";
        }
        
    }
    

}
