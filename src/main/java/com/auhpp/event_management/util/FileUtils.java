package com.auhpp.event_management.util;

import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUtils {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public static void validateFile(MultipartFile file) {

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_NAME);
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new AppException(ErrorCode.INVALID_EXTENSION);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.NOT_AN_IMAGE);
        }
    }
}
