package com.blog.storage;

import com.blog.exception.BlogException;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class GoogleCloudStorage {
    public static final String DEFAULT_IMAGE_URL = ""; // TODO: add default url

    private static final String BUCKET_NAME = "zenith-blog-storage";
    private static final Storage STORAGE = GoogleCloudStorageConfig.storage();

    public static String uploadImage(MultipartFile image) throws BlogException {
        if (image == null) {
            return DEFAULT_IMAGE_URL;
        }

        String imageName = UUID.randomUUID().toString();
        String imageExtension = getImageExtension(image.getOriginalFilename());
        String blobName = imageName + "." + imageExtension;

        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, blobName).build();
        try {
            STORAGE.create(blobInfo, image.getBytes());
        } catch (IOException e) {
            throw new BlogException("Failed to upload image to Google Cloud Storage.");
        }

        // If your bucket is public, you can use this URL format
        return String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, blobName);
    }

    private static String getImageExtension(String originalFilename) {
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // or a default extension
        }
        return originalFilename.substring(lastDotIndex + 1);
    }
}
