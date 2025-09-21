package com.messUp.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class MediaUploadService {

    private final Cloudinary cloudinary;

    public MediaUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadMedia(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        try {
            String contentType = file.getContentType();

            Map<String, Object> options = new HashMap<>();
            if(contentType!=null){
                if(contentType.startsWith("video/")) {
                    options.put("resource_type", "video");
                } else if(contentType.startsWith("audio/")) {
                    options.put("resource_type", "video");
                } else {
                    options.put("resource_type", "image");
                }
            }
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }
}
