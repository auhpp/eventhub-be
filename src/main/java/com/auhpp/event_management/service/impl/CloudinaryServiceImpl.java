package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.util.FileUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    Cloudinary cloudinary;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public Map<String, Object> uploadFile(MultipartFile file, String folderName) {
        FileUtils.validateFile(file);
        try {
            return cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folderName,
                            "resource_type", "auto"
                    )
            );

        } catch (Exception e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public List<Map<String, Object>> uploadMultipleFiles(List<MultipartFile> files, String folderName) {
        List<CompletableFuture<Map<String, Object>>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(
                        () -> uploadFile(file, folderName), executorService
                )).toList();
        return futures.stream().map(
                CompletableFuture::join
        ).toList();
    }

    @Override
    public Map<String, Object> deleteFile(String publicId) {
        try {
            return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new AppException(ErrorCode.FILE_DELETE_FAILED);
        }
    }


}
