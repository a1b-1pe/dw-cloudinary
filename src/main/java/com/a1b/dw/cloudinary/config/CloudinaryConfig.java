package com.a1b.dw.cloudinary.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudname}")
    private String cloudname;
    @Value("${apikey}")
    private String apikey;
    @Value("${apisecret}")
    private String apisecret;

    private Cloudinary cloudinary;

    @PostConstruct
    public void init() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudname,
            "api_key", apikey,
            "api_secret", apisecret));
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    public Map<?, ?> fileUpload(File file) throws IOException {
        return cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
    }

    public Map<?, ?> docUpload(File file) throws IOException {
        return cloudinary.uploader().upload(file,
            ObjectUtils.asMap(
                "resource_type", "raw",
                "folder", "documents",
                "use_filename", true,
                "format", "pdf",
                "content_disposition", "inline"
            )
        );
    }

    public Map<?, ?> zipFileUpload(File file) throws IOException {
        return cloudinary.uploader().upload(file,
            ObjectUtils.asMap(
                "resource_type", "raw",
                "folder", "archives"
            )
        );
    }

    public Map<?, ?> videoUpload(File file) throws IOException {
        return cloudinary.uploader().upload(file,
            ObjectUtils.asMap(
                "resource_type", "video",
                "chunk_size", 24000000,
                "eager", Arrays.asList(
                    new Transformation().width(300).height(300).crop("pad").audioCodec("none"),
                    new Transformation().width(160).height(100).crop("crop").gravity("south").audioCodec("none")),
                "eager_async", true
            )
        );
    }

    public Map<?, ?> fileUploadById(File file, String publicId) throws IOException {
        return cloudinary.uploader().upload(file,
            ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "invalidate", true,
                "resource_type", "image"
            )
        );
    }

    public String createThumbnail(String publicId) {
        return cloudinary.url()
            .transformation(new Transformation().width(150).height(150).crop("thumb"))
            .generate(publicId);
    }

    public String createVideoThumbnail(String publicId) {
        return cloudinary.url()
            .resourceType("video")
            .transformation(new Transformation().width(200).height(150).crop("fill"))
            .format("png")
            .generate(publicId);
    }
}
