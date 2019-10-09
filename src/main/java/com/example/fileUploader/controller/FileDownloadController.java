package com.example.fileUploader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.fileUploader.service.FileStorageService;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class FileDownloadController {

	private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
	
	@Autowired
	private FileStorageService fileStorageService;
	
	
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
		
		//Load file as resource
		Resource resource =  fileStorageService.loadFileResource(fileName);
		
		 // Try to determine file's content type
		String contentType = null;
		
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			
		} catch (IOException e) {
			 logger.info("Could not determine file type.");
		}
		
		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
	        contentType = "application/octet-stream";
		}
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
		
		
	}
	
}
