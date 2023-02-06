package com.example.springbootsampleec.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 画像のアップロード
     * @param multipartFile
     * @return アップロードした画像のファイル名 String
     */
    public String uploadImage(MultipartFile multipartFile);
}
