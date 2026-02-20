package com.a1b.dw.cloudinary.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseEntity<Map> uploadImage(@RequestParam(value="file", required=true) MultipartFile aFile)
	{
		try
        {
			logger.info(aFile.getSize());
            File f=Files.createTempFile("temp", aFile.getOriginalFilename()).toFile();
            aFile.transferTo(f);
            Map<?, ?> response=cloudinaryConfig.fileUpload(f);
            return ResponseEntity.ok()
            		.body(response);
        }
		catch(Exception e)
        {
            logger.info(e.getMessage());
        }
		return null;
	}
	
	@PostMapping(value = "/zip")
	public ResponseEntity<Map> uploadZip(@RequestParam(value="file", required=true) MultipartFile aFile)
	{
		try
        {
			logger.info(aFile.getSize());
            File f=Files.createTempFile("temp", aFile.getOriginalFilename()).toFile();
            aFile.transferTo(f);
            Map<?, ?> response=cloudinaryConfig.zipFileUpload(f);
            return ResponseEntity.ok().body(response);
        }
		catch(Exception e)
        {
            logger.info(e.getMessage());
        }
		return null;
	}
	
	@PostMapping(value = "/video")
	public ResponseEntity<Map> uploadVideo(@RequestParam(value="file", required=true) MultipartFile aFile)
	{
		try
        {
			logger.info(aFile.getSize());
            File f=Files.createTempFile("temp", aFile.getOriginalFilename()).toFile();
            aFile.transferTo(f);
            Map response=cloudinaryConfig.videoUpload(f);
            return ResponseEntity.ok().body(response);
		
        }
		catch(Exception e)
        {
            logger.info("error occured",e.getMessage());
        }
		return null;
	}
	
	@PutMapping(value = "/image/id/{publicId}")
	public ResponseEntity<Map> fileUploadById(@RequestParam(value="file", required=true) MultipartFile aFile,
			@PathVariable String publicId)
	{
		try
        {
			logger.info(aFile.getSize());
            File f=Files.createTempFile("temp", aFile.getOriginalFilename()).toFile();
            
            aFile.transferTo(f);
            Map response=cloudinaryConfig.fileUploadById(f,publicId);
            return ResponseEntity.ok()
            		//.headers(responseHeaders)
            		.body(response);
		
        }
		catch(Exception e)
        {
            logger.info("error occured",e.getMessage());
        }
		return null;
	}
	
	@DeleteMapping(value = "/image/id/{publicId}")
	public DeleteR deleteImageByPublicId(@PathVariable String publicId,String version)
	{
		 DeleteR deleteR = new DeleteR();
		Map<String,String> options = new HashMap<String,String>();
	    try {
			cloudinaryConfig.getCloudinary().uploader().destroy(publicId,options);
			deleteR.setDeleteMessage("public_id deleted successfully");
			deleteR.setIsdeleted(true);
		} catch (IOException e) {
			//implement logging service
			logger.info("error occured",e.getMessage());
		}
	   
 		return deleteR;
		
	}
	
	@GetMapping(value = "/video/id/{publicId}")
	public String createVideoThumbnail(@PathVariable String publicId)
	{
		return cloudinaryConfig.createVideoThumbnail(publicId);
	}

	@GetMapping(value = "/image/id/{publicId}")
	public String createThumbnail(@PathVariable String publicId)
	{
		return cloudinaryConfig.createThumbnail(publicId);
	}
}
