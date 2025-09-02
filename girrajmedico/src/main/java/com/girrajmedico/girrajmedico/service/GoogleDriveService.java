package com.girrajmedico.girrajmedico.service;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.FileContent;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;

@Service
public class GoogleDriveService {

//    private static final String APPLICATION_NAME = "YourAppName"; // Replace with your app name
//    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE); // Scope for creating and modifying files
//
//    @Value("${google.credentials.file}")
//    private String credentialsFilePath; // Path to your downloaded JSON key file
//
//    private Credential authorize() throws IOException, GeneralSecurityException {
//        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(credentialsFilePath));
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);
//
//        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
//                .setAccessType("offline") // For getting a refresh token
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build(); // Choose an unused port
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//    }
//
//    private Drive getDriveService() throws IOException, GeneralSecurityException {
//        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        Credential credential = authorize();
//        return new Drive.Builder(httpTransport, JSON_FACTORY, credential)
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//
//    public String uploadImage(java.io.File imageFile, String fileName, String mimeType, String parentFolderId) {
//        try {
//            Drive driveService = getDriveService();
//            File fileMetadata = new File();
//            fileMetadata.setName(fileName);
//            if (parentFolderId != null && !parentFolderId.isEmpty()) {
//                fileMetadata.setParents(Collections.singletonList(parentFolderId));
//            }
//            FileContent mediaContent = new FileContent(mimeType, imageFile);
//            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
//                    .setFields("id, name, webContentLink")
//                    .execute();
//            return uploadedFile.getId(); // Or return the webContentLink
//        } catch (IOException | GeneralSecurityException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // Example usage in a controller:
//    // @PostMapping("/upload")
//    // public ResponseEntity<String> uploadImageToDrive(@RequestParam("file") MultipartFile file,
//    //                                                 @RequestParam(value = "parent", required = false) String parentFolderId) {
//    //     try {
//    //         java.io.File tempFile = File.createTempFile("uploadedImage", file.getOriginalFilename());
//    //         file.transferTo(tempFile);
//    //         String mimeType = file.getContentType();
//    //         String fileId = googleDriveService.uploadImage(tempFile, file.getOriginalFilename(), mimeType, parentFolderId);
//    //         tempFile.delete(); // Clean up temporary file
//    //         if (fileId != null) {
//    //             return ResponseEntity.ok("Image uploaded successfully. File ID: " + fileId);
//    //         } else {
//    //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
//    //         }
//    //     } catch (IOException e) {
//    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file.");
//    //     }
//    // }
}