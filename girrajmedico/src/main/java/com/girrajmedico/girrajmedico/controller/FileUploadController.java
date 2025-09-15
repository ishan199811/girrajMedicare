package com.girrajmedico.girrajmedico.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption; // Import the necessary class

@RestController
public class FileUploadController {

    private static final String UPLOAD_DIR = "/Users/ishansharma/Desktop/Teqtron/Teq_SaleSwift_SAP_RE_SAP/Teq_SaleSwift_SAP_RE_EBS/b2b_ui_sap1/public/files/"; // Replace with your desired directory

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }

        try {
            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Resolve the full path to the file
            Path filePath = uploadPath.resolve(file.getOriginalFilename());

            // Save the file to the file system and rewrite if it exists
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("File uploaded successfully, existing file was rewritten: " + file.getOriginalFilename());

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload the file due to an error: " + e.getMessage());
        }
    }
}