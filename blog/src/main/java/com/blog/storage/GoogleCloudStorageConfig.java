package com.blog.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleCloudStorageConfig {

    @Value("${google.cloud.storage.credentials.path}")
    private static String credentialsPath;

    @Bean
    public static Storage storage() {
        try {
            return StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentialsPath)))
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new Error(e.getMessage());
        }
    }
}