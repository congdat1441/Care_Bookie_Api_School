package com.spring.carebookie.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    /**
     * Save file to s3
     *
     * @param path        bucket name
     * @param fileName
     * @param inputStream
     * @param optMetadata
     */
    public void save(String path, String fileName, InputStream inputStream, Optional<Map<String, String>> optMetadata) {
        ObjectMetadata metadata = new ObjectMetadata();
        optMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata);
            }
        });
        try {
            amazonS3.putObject(path, fileName, inputStream, metadata);
        } catch (AmazonServiceException exception) {
            exception.printStackTrace();
            throw new IllegalStateException("Fail to store file to s3");
        }
    }

    /**
     * Download image file from s3 <br>
     *
     * @param path bucket name
     * @param key  file name
     * @return byte[]
     */
    public byte[] download(String path, String key) {
        try {
            S3Object s3Object = amazonS3.getObject(path, key);
            S3ObjectInputStream objectContent = s3Object.getObjectContent();
            return IOUtils.toByteArray(objectContent);
        } catch (AmazonServiceException | IOException exception) {
            throw new IllegalStateException("Failed to download  file from s3");
        }
    }
}
