package com.a1b.dw.cloudinary.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.a1b.dw.cloudinary.config.CloudinaryConfig;
import com.a1b.dw.cloudinary.vo.DeleteR;

@RestController
@RequestMapping(value = "/v1/cloudinary")
@CrossOrigin(origins = {
    "https://dayworks.co",
    "https://workday24-fd282.web.app",
    "http://localhost:8080",
    "http://localhost:5173",
    "https://localhost:5173"
})
public class CloudinaryController {

    private static final Logger logger = LogManager.getLogger(CloudinaryController.class);

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    @PostMapping(value = "/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile aFile) {
        String ct = aFile.getContentType();
        if (ct == null || !ct.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only image files are allowed"));
        }
        File f = null;
        try {
            f = Files.createTempFile("upload-", aFile.getOriginalFilename()).toFile();
            aFile.transferTo(f);
            return ResponseEntity.ok(cloudinaryConfig.fileUpload(f));
        } catch (Exception e) {
            logger.error("Image upload failed", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed"));
        } finally {
            if (f != null) f.delete();
        }
    }

    @PostMapping(value = "/pdf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile aFile) {
        if (!"application/pdf".equals(aFile.getContentType())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only PDF files are allowed"));
        }
        File f = null;
        try {
            f = Files.createTempFile("upload-", ".pdf").toFile();
            aFile.transferTo(f);
            return ResponseEntity.ok(cloudinaryConfig.docUpload(f));
        } catch (Exception e) {
            logger.error("PDF upload failed", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed"));
        } finally {
            if (f != null) f.delete();
        }
    }

    @PostMapping(value = "/zip")
    public ResponseEntity<?> uploadZip(@RequestParam("file") MultipartFile aFile) {
        String ct = aFile.getContentType();
        if (ct == null || (!ct.equals("application/zip")
                && !ct.equals("application/x-zip-compressed")
                && !ct.equals("application/octet-stream"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only ZIP files are allowed"));
        }
        File f = null;
        try {
            f = Files.createTempFile("upload-", ".zip").toFile();
            aFile.transferTo(f);
            return ResponseEntity.ok(cloudinaryConfig.zipFileUpload(f));
        } catch (Exception e) {
            logger.error("ZIP upload failed", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed"));
        } finally {
            if (f != null) f.delete();
        }
    }

    @PostMapping(value = "/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile aFile) {
        String ct = aFile.getContentType();
        if (ct == null || !ct.startsWith("video/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only video files are allowed"));
        }
        File f = null;
        try {
            f = Files.createTempFile("upload-", aFile.getOriginalFilename()).toFile();
            aFile.transferTo(f);
            return ResponseEntity.ok(cloudinaryConfig.videoUpload(f));
        } catch (Exception e) {
            logger.error("Video upload failed", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed"));
        } finally {
            if (f != null) f.delete();
        }
    }

    @PutMapping(value = "/image/id/{publicId}")
    public ResponseEntity<?> fileUploadById(@RequestParam("file") MultipartFile aFile,
            @PathVariable String publicId) {
        String ct = aFile.getContentType();
        if (ct == null || !ct.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only image files are allowed"));
        }
        File f = null;
        try {
            f = Files.createTempFile("upload-", aFile.getOriginalFilename()).toFile();
            aFile.transferTo(f);
            return ResponseEntity.ok(cloudinaryConfig.fileUploadById(f, publicId));
        } catch (Exception e) {
            logger.error("Image update failed for publicId: {}", publicId, e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed"));
        } finally {
            if (f != null) f.delete();
        }
    }

    @DeleteMapping(value = "/image/id/{publicId}")
    public ResponseEntity<?> deleteImageByPublicId(@PathVariable String publicId) {
        DeleteR deleteR = new DeleteR();
        try {
            cloudinaryConfig.getCloudinary().uploader().destroy(publicId, new HashMap<>());
            deleteR.setDeleteMessage("public_id deleted successfully");
            deleteR.setIsdeleted(true);
            return ResponseEntity.ok(deleteR);
        } catch (IOException e) {
            logger.error("Delete failed for publicId: {}", publicId, e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Delete failed"));
        }
    }

    @GetMapping(value = "/video/id/{publicId}")
    public ResponseEntity<String> createVideoThumbnail(@PathVariable String publicId) {
        return ResponseEntity.ok(cloudinaryConfig.createVideoThumbnail(publicId));
    }

    @GetMapping(value = "/image/id/{publicId}")
    public ResponseEntity<String> createThumbnail(@PathVariable String publicId) {
        return ResponseEntity.ok(cloudinaryConfig.createThumbnail(publicId));
    }
}
