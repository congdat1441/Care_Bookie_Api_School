package com.spring.carebookie.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.carebookie.UserProfileEntity;
import com.spring.carebookie.enumeration.BucketName;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class S3Services {
    private static final List<UserProfileEntity> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfileEntity(UUID.fromString("44f185f4-4633-478a-a505-6dae187d9494"), "oanh", null));
        USER_PROFILES.add(new UserProfileEntity(UUID.fromString("d9810e31-921f-4ed5-ad61-041292a08bc1"), "oanh1", null));
    }

    private S3Service s3Service;

//    public java.util.List<BookTypeEntity> getAllBookTypes() {
//        return testRepository.findAll();
//    }

    public List<UserProfileEntity> getUserProfiles() {
        return USER_PROFILES;
    }

    /**
     * Upload image of user to s3 <br>
     * 1/ Check if image is not empty <br>
     * 2/ If file is an image <br>
     * 3/ The user exists in our database <br>
     * 4/ Grab some metadata from file if any <br>
     * 5/ Store the image into s3 and update (userProfileImageLink) with  s3 image link
     *
     * @param userProfileId
     * @param file
     */
    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalStateException("File must be not empty");
        }

        if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(), ContentType.IMAGE_SVG.getMimeType(),
                        ContentType.IMAGE_GIF.getMimeType(), ContentType.IMAGE_PNG.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("This file is not image");
        }

        UserProfileEntity userProfile = getUserProfiles().stream()
                .filter(u -> u.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User %s is not found", userProfileId)));

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = BucketName.PROFILE_IMAGE.getBucketName();
        String fileName = String.format("%s--%s", userProfileId, file.getOriginalFilename());
        try {
            s3Service.save(path, fileName, file.getInputStream(), Optional.of(metadata));
            userProfile.setUserProfileImageLink(fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Download file from s3 <br>
     *
     * @param userProfileId
     * @return byte[]
     */
    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfileEntity userProfile = getUserProfiles().stream()
                .filter(u -> u.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User %s is not found", userProfileId)));

        String path = BucketName.PROFILE_IMAGE.getBucketName();

        return userProfile.getUserProfileImageLink()
                .map(key -> s3Service.download(path, key))
                .orElse(new byte[0]);
    }
}
