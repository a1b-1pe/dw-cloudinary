package com.a1b.dw.cloudinary.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

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
	
	Cloudinary cloudinary = null;
	
	public Cloudinary getCloudinary()
	{
		
		cloudinary = new Cloudinary(ObjectUtils.asMap(
				  "cloud_name", cloudname,
				  "api_key", apikey,
				  "api_secret", apisecret));
		return cloudinary;
	}
	
	
	public Map<?, ?> fileUpload(File file)
	{
		cloudinary = getCloudinary();
		File toUpload = file;
		try {
			return cloudinary.uploader().upload(toUpload, 
					ObjectUtils.emptyMap());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyMap();
	}
	
	public Map<?, ?> zipFileUpload(File file)
	{
		cloudinary = getCloudinary();
		String uploadFileName = "okeezip";
		File toUpload = file;
		try {
			return cloudinary.uploader().upload(toUpload, 
					ObjectUtils.asMap(
							"resource_type","raw",
							 "public_id", uploadFileName
							));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<?, ?> videoUpload(File file)
	{
		cloudinary = getCloudinary();
		File toUpload = file;
		try {
			return cloudinary.uploader().upload(toUpload, 
			    ObjectUtils.asMap("resource_type", "video",
			    				  "chunk_size", 24000000,
			    "eager", Arrays.asList(
			        new Transformation().width(300).height(300).crop("pad").audioCodec("none"),
			        new Transformation().width(160).height(100).crop("crop").gravity("south").audioCodec("none")),
			    "eager_async", true,
			    "eager_notification_url", "https://taherlal.com/notify_endpoint"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Map<?, ?> fileUploadById(File file,String publicId)
	{
		cloudinary = getCloudinary();
		Map params = ObjectUtils.asMap(
			    "public_id", publicId, 
			    "overwrite", true,
			    "invalidate", true,
			    "notification_url", "https://hmstore-notification-dev.herokuapp.com/v1/notification/token",
			    "resource_type", "image"         
			);
		File toUpload = file;
		try {
			return cloudinary.uploader().upload(toUpload,params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyMap();
	}
	
	// thumbnail for image
	public String createThumbnail(String publicId) {
		cloudinary = getCloudinary();
		 // Generate and display the URL for the thumbnail image
        String thumbnailUrl = cloudinary.url().transformation(
                new Transformation().width(150).height(150).crop("thumb")
        ).generate(publicId);

        System.out.println("Thumbnail URL: " + thumbnailUrl);
        return thumbnailUrl;
		/*return cloudinary.url().resourceType("video").transformation(new Transformation().
				width(200).height(150).crop("fill")).format("png").generate(publicId);*/
	}
	
	// thumbnail for image
		public String createVideoThumbnail(String publicId) {
			cloudinary = getCloudinary();
			return cloudinary.url().resourceType("video").transformation(new Transformation().
					width(200).height(150).crop("fill")).format("png").generate(publicId);
		

		}
}
