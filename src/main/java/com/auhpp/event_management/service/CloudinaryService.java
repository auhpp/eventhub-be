package com.auhpp.event_management.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CloudinaryService {
    Map<String, Object> uploadFile(MultipartFile file, String folderName);

    List<Map<String, Object>> uploadMultipleFiles(List<MultipartFile> files, String folderName);

    Map<String, Object> deleteFile(String publicId);


}
