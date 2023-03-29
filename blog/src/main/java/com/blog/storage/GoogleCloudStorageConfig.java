package com.blog.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCloudStorageConfig {

    @Value("${google.cloud.storage.credentials.path}")
    private String credentialsPath;

    @Bean
    public Storage storage() {
        try {
            InputStream credentialsStream = new ClassPathResource(credentialsPath).getInputStream();
            return StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new Error(e.getMessage());
        }
    }
}
