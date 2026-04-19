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

import java.io.IOException;
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
    public Map<String, Object> uploadFile(MultipartFile file, String folderName, boolean isImage) {
        if (isImage) {
            FileUtils.validateImageFile(file);
        } else {
            FileUtils.validateAudioFile(file);
        }
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
    public List<Map<String, Object>> uploadMultipleFiles(List<MultipartFile> files, String folderName, boolean isImage) {
        List<CompletableFuture<Map<String, Object>>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(
                        () -> uploadFile(file, folderName, isImage), executorService
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

    @Override
    public String uploadPdf(byte[] pdfBytes, String folderName, String fileName) throws IOException {
        Map<String, Object> params = ObjectUtils.asMap(
                "resource_type", "image",
                "folder", folderName,
                "format", "pdf",
                "public_id", fileName
        );
        Map uploadResult = cloudinary.uploader().upload(pdfBytes, params);
        return uploadResult.get("secure_url").toString();
    }


}
