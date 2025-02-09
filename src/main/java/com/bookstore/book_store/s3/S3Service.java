package com.bookstore.book_store.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.accesskey}")
    private String awsKey;

    
    public String uploadImageToS3(String imageUrl, String title) {
        try {
            // Download the image
            URL url = new URL(imageUrl);
            File tempFile;
            try (InputStream inputStream = url.openStream()) {
                tempFile = File.createTempFile(title, ".jpg");
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                byte[] buffer = new byte[2048];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }   outputStream.close();
            }

            // Upload the image to S3
            String key = "bookcovers/" + tempFile.getName();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(title)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(tempFile));

            // Return the S3 URL
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();
        } catch (IOException e) {
            return "Error uploading file";
        }
    }
}
