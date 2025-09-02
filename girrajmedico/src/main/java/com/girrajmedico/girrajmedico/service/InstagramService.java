package com.girrajmedico.girrajmedico.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class InstagramService {

    @Value("${instagram.api.base-url}")
    private String instagramApiBaseUrl;

    @Value("${instagram.access-token}")
    private String accessToken; // Store your access token securely

    private final RestTemplate restTemplate;

    public InstagramService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String uploadPhoto(String imageUrl, String caption) {
        try {
            // 1. Create an image container
            String imageUploadId = createImageContainer(imageUrl);
            if (imageUploadId != null) {
                // 2. Publish the image container
                return publishImage(imageUploadId, caption);
            }
            return null;
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return null;
        }
    }

    private String createImageContainer(String imageUrl) {
        String url = instagramApiBaseUrl + "/me/media";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image_url", imageUrl);
        body.add("access_token", accessToken);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ImageContainerResponse> response = restTemplate.postForEntity(url, requestEntity, ImageContainerResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getId() != null) {
            return response.getBody().getId();
        } else {
            // Handle error
            System.err.println("Error creating image container: " + response.getStatusCode() + " - " + response.getBody());
            return null;
        }
    }

    private String publishImage(String imageUploadId, String caption) {
        String url = instagramApiBaseUrl + "/me/media_publish";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("creation_id", imageUploadId);
        body.add("caption", caption);
        body.add("access_token", accessToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<PublishResponse> response = restTemplate.postForEntity(url, requestEntity, PublishResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getId() != null) {
            return response.getBody().getId();
        } else {
            // Handle error
            System.err.println("Error publishing image: " + response.getStatusCode() + " - " + response.getBody());
            return null;
        }
    }

    // Define response classes (inner classes for simplicity)
    private static class ImageContainerResponse {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    private static class PublishResponse {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}