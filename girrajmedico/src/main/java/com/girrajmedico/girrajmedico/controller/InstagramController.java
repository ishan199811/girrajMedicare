package com.girrajmedico.girrajmedico.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.service.InstagramService;

@RestController
public class InstagramController {

    private final InstagramService instagramService;

    public InstagramController(InstagramService instagramService) {
        this.instagramService = instagramService;
    }

    @PostMapping("/post-to-instagram")
    public ResponseEntity<String> postToInstagram(@RequestParam String imageUrl, @RequestParam String caption) {
        String postId = instagramService.uploadPhoto(imageUrl, caption);
        if (postId != null) {
            return ResponseEntity.ok("Successfully posted to Instagram with ID: " + postId);
        } else {
            return ResponseEntity.badRequest().body("Failed to post to Instagram.");
        }
    }
}