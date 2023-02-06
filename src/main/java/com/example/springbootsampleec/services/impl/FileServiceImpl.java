package com.example.springbootsampleec.services.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.springbootsampleec.services.FileService;



@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private Environment environment;
	
	@Transactional
	@Override
    public String uploadImage(MultipartFile image) {
		if(image.getOriginalFilename().isEmpty()) {
			throw new RuntimeException("ファイルが設定されていません");
		}
		String extension = FilenameUtils.getExtension(image.getOriginalFilename());
		String randomFileName = RandomStringUtils.randomAlphanumeric(20) + "." + extension;
		Path filePath = Paths.get(environment.getProperty("sample.images.imagedir") + randomFileName);
		try {
			byte[] bytes = image.getBytes();
			OutputStream stream = Files.newOutputStream(filePath);
			stream.write(bytes);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return randomFileName;
	} 
}
