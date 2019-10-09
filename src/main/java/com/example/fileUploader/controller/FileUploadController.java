package com.example.fileUploader.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.fileUploader.service.FileStorageService;

@RestController
public class FileUploadController {

	@Autowired
	private FileStorageService fileStorageService;
	
	@PostMapping("/uploadFile")
	public Response uploadFile(@RequestParam("file") MultipartFile file) {
		
		String fileName = fileStorageService.storeFile(file);
		
		String fileDounloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/downloadFile/")
				.path(fileName)
				.toUriString();
		
		System.out.println(fileDounloadUri.toString());
		
		return new Response(fileName,fileDounloadUri,file.getContentType(),file.getSize());
		
	}
	
	@PostMapping("/uploadMultipartFiles")
	public List<Response> uploadMultipartFiles(@RequestParam("files") MultipartFile[] files){
		
		return Arrays.asList(files)
				.stream()
				.map(file -> uploadFile(file))
				.collect(Collectors.toList());
		
	}
	
	
}
